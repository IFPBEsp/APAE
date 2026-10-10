"use client";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/components/ui/form";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/domains/patients/hooks/use-members-register-context";
import { Profile } from "@/domains/patients/schemas/member-schemas";
import { EditProfile } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";

import z from "zod";
import { FormButton, MembersRegisterForm } from "../form";
import { usePhotoUpload } from "@/hooks/use-photo-upload";
import { PhotoUpload } from "@/components/shared/PhotoUpload";
import { Checkbox } from "@/components/ui/checkbox";
import { useRouter, useParams, usePathname } from "next/navigation";
import { toast } from "react-toastify";

export default function MembersRegisterProfilePage() {
  const {
    state: { profile },
    setters: { setProfileData, setStep },
    register,
  } = useMembersRegisterContext();

  const [submitted, setSubmitted] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  
  const {
    fileInputRef,
    selectedPhoto,
    setSelectedPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
    clearPhoto,
    uploadPhoto,
  } = usePhotoUpload({ buildUrl: (patientId) => `/apae-geral/api/patients/${patientId}/photo`, method: "PUT", fieldName: "photo" });
  const router = useRouter();

  const params = useParams();
  const pathname = usePathname();
  const id = params?.id as string;
  const isEditing = pathname.includes("/edit");

  const currentSchema = isEditing ? EditProfile : Profile;

  const form = useForm<z.infer<typeof currentSchema>>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: profile,
  });

  const [isInitialized, setIsInitialized] = useState(false);

  const getErrorMessage = (data: any) => {
    if (!data) return "Erro inesperado no servidor.";
    if (typeof data === "string") return data;
    if (typeof data.message === "string") return data.message;
    if (data.message && typeof data.message.message === "string") {
      return data.message.message;
    }
    if (typeof data.error === "string") return data.error;
    return "Erro inesperado no servidor.";
  };

  useEffect(() => {
    if (isEditing && profile.role && !isInitialized) {
      form.reset(profile);
      setIsInitialized(true);
    }
  }, [profile, form, isEditing, isInitialized]);

  useEffect(() => {
    if (profile.role || profile.photo instanceof File) {
      form.reset(profile);
    }
  }, [profile, form]);

  useEffect(() => {
    if (photoError) {
      toast.error(photoError);
    }
  }, [photoError]);

  // Fetches the patient's current photo when entering edit mode
  useEffect(() => {
    if (!isEditing || !id) return;
    if (profile.photo) return;

    (async () => {
      try {
        const res = await fetch(`/apae-geral/api/patients/${id}`);
        const data = await res.json();
        if (data?.photoUrl) {
          setProfileData({ photo: data.photoUrl });
        }
      } catch (e) {
        console.error("Erro ao buscar foto do paciente:", e);
      }
    })();
  }, [isEditing, id]);

  useEffect(() => {
    if (selectedPhoto instanceof File) {
      setProfileData({ photo: selectedPhoto });
      return;
    }
    // selectedPhoto virou null (foto removida): limpa o contexto
    // APENAS se ele guarda um File local — nunca apaga a URL (string)
    // da foto existente carregada do servidor na edição.
    if (profile.photo instanceof File) {
      setProfileData({ photo: undefined });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedPhoto, setProfileData]);

  useEffect(() => {
    if (profile?.photo instanceof File && !selectedPhoto) {
      setSelectedPhoto(profile.photo);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    if (submitted && profile) {
      setSubmitted(false);
      (async () => {
        setIsLoading(true);
        try {
          const res = await register(id);

          if (res.status === 201 || res.status === 200 || res.status === 204) {
            if (isEditing && selectedPhoto) {
              const ok = await uploadPhoto(id);
              if (!ok) {
                setSubmitted(false);
                setIsLoading(false);
                return;
              }
            }

            toast.success(
              isEditing
                ? "Paciente atualizado com sucesso!"
                : "Membro cadastrado com sucesso!",
            );
            router.push(
              isEditing ? `/patients/${id}` : "/patients",
            );
            return;
          }

          if (res.status === 409) {
            const msg = getErrorMessage(res.data);
            const msgLower = msg.toLowerCase();

            let targetField = "cpf";
            let displayMsg = "CPF ou documento já cadastrado no sistema.";

            if (msgLower.includes("rg") || msgLower.includes("identidade")) {
              targetField = "rg.number";
              displayMsg = "Este RG já está cadastrado no sistema.";
            } else if (msgLower.includes("cns")) {
              targetField = "cns";
              displayMsg = "Este CNS já está cadastrado no sistema.";
            } else if (msgLower.includes("cpf")) {
              targetField = "cpf";
              displayMsg = "Este CPF já está cadastrado no sistema.";
            }

            toast.error(displayMsg);
            setStep(MembersRegisterStep.PERSONAL);
            form.setError(targetField as any, {
              type: "manual",
              message: displayMsg,
            });
            setSubmitted(false);
            return;
          }

          if (res.status === 400) {
            const resData = res.data as { fields?: Array<{ field?: string; message?: string }> } | undefined;
            const firstError = resData?.fields?.[0];
            const backendField = firstError?.field || "";
            const fieldLower = backendField.toLowerCase();
            const errorMessage =
              firstError?.message || getErrorMessage(res.data);

            if (backendField) {
              if (
                ["fullName", "cpf", "rg", "contact", "birth", "nationality", "cns", "nis", "phone", "name"]
                  .some((f) => fieldLower.includes(f.toLowerCase()))
              ) {
                setStep(MembersRegisterStep.PERSONAL);
              } else if (fieldLower.includes("parents") || fieldLower.includes("kinships")) {
                setStep(MembersRegisterStep.KINSHIPS);
              } else if (fieldLower.includes("address") && !fieldLower.includes("guardian")) {
                setStep(MembersRegisterStep.ADDRESS);
              } else if (
                ["annualRegistry", "vaccine", "allergies", "diseases", "familyIncome", "householdIncome"]
                  .some((f) => fieldLower.includes(f.toLowerCase()))
              ) {
                setStep(MembersRegisterStep.ADDITIONALS);
              } else if (fieldLower.includes("guardian")) {
                setStep(MembersRegisterStep.GUARDIAN);
              }
            }

            toast.error(errorMessage);
            handleBackendValidationErrors(res.data, form.setError);
            setSubmitted(false);
            return;
          }

          toast.error(getErrorMessage(res.data));
          setSubmitted(false);
        } catch (error) {
          toast.error("Falha na conexão com o servidor.");
          setSubmitted(false);
        } finally {
          setIsLoading(false);
        }
      })();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [submitted, profile, register, router, form, setStep, id, isEditing]);

  const onSubmit = async (values: z.infer<typeof currentSchema>) => {
    const payload = { ...values };
    if (isEditing && payload.photo instanceof File) {
      payload.photo = undefined;
    }
    setProfileData(payload);
    setSubmitted(true);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Finalizar Edição" : "Informações Importantes"}
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => {
                const destino = isEditing
                  ? MembersRegisterStep.ADDRESS
                  : MembersRegisterStep.ADDITIONALS;
                setStep(destino);
              }}
              disabled={isLoading}
            >
              Voltar
            </FormButton>

            <FormButton type="submit" disabled={isLoading}>
              {isLoading
                ? "Salvando..."
                : isEditing
                  ? "Salvar Alterações"
                  : "Salvar"}
            </FormButton>
          </>
        }
      >
        <div className="grid grid-cols-1 gap-6">
          <PhotoUpload
            label={`Selecione uma foto ${isEditing ? "(Opcional na edição)" : "*"}`}
            fileInputRef={fileInputRef}
            selectedPhoto={selectedPhoto}
            photoPreviewUrl={photoPreviewUrl}
            profilePhotoUrl={typeof profile.photo === 'string' ? profile.photo : null}
            photoError={photoError}
            photoSuccess={photoSuccess}
            setSelectedPhoto={setSelectedPhoto}
            clearPhoto={clearPhoto}
          />

          <FormField
            control={form.control}
            name="role"
            render={({ field }) => (
              <FormItem className="space-y-3">
                <FormLabel>
                  Selecione qual função será ocupada na aplicação? *
                </FormLabel>
                <FormItem className="flex flex-row items-center gap-2">
                  <FormControl>
                    <Checkbox
                      checked={field.value === "patient" || field.value === "student"}
                      disabled={isLoading}
                      onCheckedChange={() => field.onChange("patient")}
                    />
                  </FormControl>
                  <FormLabel className="cursor-pointer">Paciente</FormLabel>
                </FormItem>
                <FormItem className="flex flex-row items-center gap-2">
                  <FormControl>
                    <Checkbox
                      checked={field.value === "student"}
                      disabled={isLoading}
                      onCheckedChange={(checked) =>
                        field.onChange(checked ? "student" : "patient")
                      }
                    />
                  </FormControl>
                  <FormLabel className="cursor-pointer">Aluno</FormLabel>
                </FormItem>
              </FormItem>
            )}
          />
        </div>
      </MembersRegisterForm>
    </Form>
  );
}

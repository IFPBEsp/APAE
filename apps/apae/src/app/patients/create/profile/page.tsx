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
import { EditProfile } from "@/domains/patients/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";
import {getConflictError, getStepForBackendField, isSuccessStatus} from "@/domains/patients/shared/patient.utils";
import { bffFetchJson } from "@/lib/bff-client";
import z from "zod";
import { FormButton, MembersRegisterForm } from "@/domains/patients/components/form";
import { usePhotoUpload } from "@/lib/use-photo-upload";
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
    selectPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
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

  const getErrorMessage = (data: unknown): string => {
    if (!data) return "Erro inesperado no servidor.";
    if (typeof data === "string") return data;
    const obj = data as { message?: string | { message?: string }; error?: string };
    if (typeof obj.message === "string") return obj.message;
    if (obj.message && typeof obj.message.message === "string") {
      return obj.message.message;
    }
    if (typeof obj.error === "string") return obj.error;
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

  // Fetches the patient's current photo when entering edit mode
  useEffect(() => {
    if (!isEditing || !id) return;
    if (profile.photo) return;

    (async () => {
      try {
        const data = await bffFetchJson<{ photoUrl?: string }>(
          `/apae-geral/api/patients/${id}`,
        );
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
    if (!submitted || !profile) return;
    setSubmitted(false);

    type RegisterResponse = Awaited<ReturnType<typeof register>>;

    const handleSuccess = async () => {
      if (isEditing && selectedPhoto) {
        const ok = await uploadPhoto(id);
        if (!ok) {
          setSubmitted(false);
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
    };

    const handleConflict = (res: RegisterResponse) => {
      const { field, message } = getConflictError(getErrorMessage(res.data));

      toast.error(message);
      setStep(MembersRegisterStep.PERSONAL);
      form.setError(field as Parameters<typeof form.setError>[0], {
        type: "manual",
        message,
      });
      setSubmitted(false);
    };

    const handleValidationError = (res: RegisterResponse) => {
      const resData = res.data as { fields?: Array<{ field?: string; message?: string }> } | undefined;
      const firstError = resData?.fields?.[0];
      const backendField = firstError?.field || "";
      const errorMessage =
        firstError?.message || getErrorMessage(res.data);

      const step = backendField ? getStepForBackendField(backendField) : undefined;
      if (step) setStep(step);

      toast.error(errorMessage);
      handleBackendValidationErrors(res.data, form.setError);
      setSubmitted(false);
    };

    const handleResponse = async (res: RegisterResponse) => {
      if (isSuccessStatus(res.status)) {
        await handleSuccess();
        return;
      }
      if (res.status === 409) {
        handleConflict(res);
        return;
      }
      if (res.status === 400) {
        handleValidationError(res);
        return;
      }

      toast.error(getErrorMessage(res.data));
      setSubmitted(false);
    };

    (async () => {
      setIsLoading(true);
      try {
        await handleResponse(await register(id));
      } catch (error) {
        toast.error("Falha na conexão com o servidor.");
        setSubmitted(false);
      } finally {
        setIsLoading(false);
      }
    })();
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
              onClick={() => setStep(MembersRegisterStep.ADDITIONALS)}
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
            selectPhoto={selectPhoto}
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

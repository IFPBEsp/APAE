"use client";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/domains/patients/hooks/use-members-register-context";
import { Profile } from "@/domains/patients/schemas/member-schemas";
import { EditProfile } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState, useRef } from "react";
import { Avatar, AvatarImage, AvatarFallback } from "@/components/ui/avatar";
import { User } from "lucide-react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";

import z from "zod";
import { FormButton, MembersRegisterForm } from "../form";
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
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
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
    if (profile.photo instanceof File) {
      const url = URL.createObjectURL(profile.photo);
      setPreviewUrl(url);
    } else if (typeof profile.photo === 'string' && profile.photo) {
      setPreviewUrl(profile.photo);
    } else {
      setPreviewUrl(null);
    }
  }, [profile.photo]);

  useEffect(() => {
    return () => {
      if (previewUrl && previewUrl.startsWith('blob:')) {
        URL.revokeObjectURL(previewUrl);
      }
    };
  }, [previewUrl]);

  // Fetches the patient's current photo when entering edit mode
  useEffect(() => {
    if (!isEditing || !id) return;
    if (profile.photo) return;

    (async () => {
      try {
        const res = await fetch(`/apae-geral/api/patients/${id}`);
        const data = await res.json();
        if (data?.photoUrl) {
          setPreviewUrl(data.photoUrl);
          setProfileData({ photo: data.photoUrl });
        }
      } catch (e) {
        console.error("Erro ao buscar foto do paciente:", e);
      }
    })();
  }, [isEditing, id]);

  useEffect(() => {
    if (submitted && profile) {
      (async () => {
        setIsLoading(true);
        try {
          const res = await register(id);

          if (res.status === 201 || res.status === 200 || res.status === 204) {
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
  }, [submitted, profile, register, router, form, setStep, id, isEditing]);

  const onSubmit = async (values: z.infer<typeof currentSchema>) => {
    setProfileData(values);
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
          <FormField
            control={form.control}
            name="photo"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm">
                  Selecione uma foto {isEditing ? "(Opcional na edição)" : "*"}
                </FormLabel>
                <FormControl>
                  <div className="flex flex-col items-start gap-4 w-full">
                    <input
                      ref={fileInputRef}
                      type="file"
                      id={`${field.name}-upload`}
                      className="hidden"
                      accept="image/*"
                      onChange={(e) => {
                        if (e.target.files && e.target.files[0]) {
                          const file = e.target.files[0];
                          field.onChange(file);
                          setProfileData({ photo: file });
                        }
                      }}
                    />

                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="relative group mr-auto"
                    >
                      <Avatar className="w-32 h-32 border-2 border-gray-300/70 cursor-pointer transition-all group-hover:opacity-80">
                        <AvatarImage src={previewUrl || undefined} alt="Foto do paciente" />
                        <AvatarFallback className="bg-gray-100">
                          <User className="w-16 h-16 text-gray-400" />
                        </AvatarFallback>
                      </Avatar>

                      <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
                        <div className="bg-black bg-opacity-50 text-white text-xs px-2 py-1 rounded">
                          Escolher foto
                        </div>
                      </div>
                    </button>
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
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

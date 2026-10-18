"use client";

import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { User } from "lucide-react";
import type { JSX } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { useCreateProfessional } from "@/hooks/profissional/use-create-profissional";
import { useProfessionalRegisterPhoto } from "@/hooks/profissional/use-professional-register-photo";
import {
  registerSchema,
  type RegisterProfessionalFormValues,
} from "@/schemas/profissional.schema";
import { ProfessionalFormFields } from "@/domains/professional/components/ProfessionalFormFields";
import { generateAvailabilityMatrix } from "@/domains/professional/shared/disponibilidade.utils";
import { buildProfessionalPayload } from "@/domains/professional/shared/professional.utils";
import { MAX_FILE_SIZE_BYTES, MAX_FILE_SIZE_LABEL } from "@/lib/constants";

export default function ProfessionalRegister(): JSX.Element {
  const router = useRouter();
  const { create, loading, error, success } = useCreateProfessional();

  const form = useForm<RegisterProfessionalFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      fullName: "",
      email: "",
      cpf: "",
      professionalDocument: "",
      serviceArea: "",
      phone: "",
      rg: "",
      state: "",
      city: "",
      neighborhood: "",
      street: "",
      number: "",
      complement: "",
      cep: "",
      availability: generateAvailabilityMatrix([]),
    },
  });

  const photoFile = form.watch("photo");
  const { fileInputRef, previewUrl } = useProfessionalRegisterPhoto(photoFile);

  const onSubmit: SubmitHandler<RegisterProfessionalFormValues> = async (values) => {
    const formData = new FormData();
    const payload = buildProfessionalPayload(values);
    formData.append(
      "professional",
      new Blob([JSON.stringify(payload)], { type: "application/json" })
    );
    if (values.photo) {
      formData.append("profilePhoto", values.photo);
    }
    formData.append("volunteerAgreement", values.volunteerAgreement);
    formData.append("curriculum", values.curriculum);
    if (values.attachmentAny) formData.append("attachmentAny", values.attachmentAny);
    await create(formData);
  };

  return (
    <div className="p-0">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 w-full max-w-2xl"
        >
          {/* Campos comuns (fullName ... complement) + disponibilidade */}
          <ProfessionalFormFields />

          {/* A partir daqui é específico do cadastro */}
          <FormField
            control={form.control}
            name="photo"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-medium">Selecione uma foto*</FormLabel>
                <FormControl>
                  <div className="flex flex-col items-start gap-4 w-full">
                    <input
                      ref={fileInputRef}
                      type="file"
                      className="hidden"
                      accept="image/png,image/jpeg,image/jpg,image/webp"
                      onChange={(e) => {
                        const file = e.target.files?.[0];
                        if (!file) return;
                        const allowedTypes = [
                          "image/png",
                          "image/jpeg",
                          "image/jpg",
                          "image/webp",
                        ];
                        if (
                          !allowedTypes.includes(file.type) ||
                          file.size <= 0 ||
                          file.size > MAX_FILE_SIZE_BYTES
                        ) {
                          alert(
                            `Apenas imagens PNG, JPG ou WEBP até ${MAX_FILE_SIZE_LABEL} são permitidas`
                          );
                          if (fileInputRef.current) {
                            fileInputRef.current.value = "";
                          }
                          field.onChange(null);
                          return;
                        }
                        field.onChange(file);
                      }}
                    />
                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="relative group mr-auto rounded-full transition-transform hover:scale-105"
                    >
                      <Avatar className="w-32 h-32 border-2 border-dashed border-gray-300 bg-gray-50">
                        <AvatarImage src={previewUrl || ""} alt="Foto do profissional" />
                        <AvatarFallback className="bg-transparent">
                          <User className="w-12 h-12 text-gray-400" />
                        </AvatarFallback>
                      </Avatar>
                      <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity bg-black/20 rounded-full">
                        <span className="bg-white text-black text-[10px] font-bold px-2 py-1 rounded shadow-sm">
                          Escolher foto
                        </span>
                      </div>
                    </button>
                    <p className="text-xs text-gray-500">
                      PNG, JPG ou WEBP até {MAX_FILE_SIZE_LABEL}
                    </p>
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="volunteerAgreement"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Termo do Voluntário *</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) => field.onChange(e.target.files?.[0] ?? null)}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="curriculum"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Currículo *</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) => field.onChange(e.target.files?.[0] ?? null)}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="attachmentAny"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Anexo qualquer</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) => field.onChange(e.target.files?.[0] ?? null)}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {success && <p className="text-green-600">Profissional criado com sucesso!</p>}

          <div className="flex justify-end gap-4">
            <Button
              type="button"
              variant="outline"
              onClick={() => router.push("/professionals")}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              className="bg-[#0D4F97] hover:bg-blue-900"
              disabled={loading}
            >
              Cadastrar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

"use client";

import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
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
import { useCreateProfessional } from "@/hooks/profissional/use-create-profissional";
import {
  registerSchema,
  type RegisterProfessionalFormValues,
} from "@/schemas/profissional.schema";
import { ProfessionalFormFields } from "@/domains/professional/components/ProfessionalFormFields";
import { generateAvailabilityMatrix } from "@/domains/professional/shared/disponibilidade.utils";
import { buildProfessionalPayload } from "@/domains/professional/shared/professional.utils";
import { usePhotoUpload } from "@/hooks/use-photo-upload";
import { PhotoUpload } from "@/components/shared/PhotoUpload";

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

  const {
    fileInputRef,
    selectedPhoto,
    selectPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
  } = usePhotoUpload();

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

          <PhotoUpload
            fileInputRef={fileInputRef}
            selectedPhoto={selectedPhoto}
            photoPreviewUrl={photoPreviewUrl}
            photoError={photoError}
            photoSuccess={photoSuccess}
            selectPhoto={selectPhoto}
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

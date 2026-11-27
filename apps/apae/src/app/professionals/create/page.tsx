"use client";

import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { JSX } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { DocumentUpload } from "@/components/shared/DocumentUpload";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
} from "@/components/ui/form";
import { useCreateProfessional } from "@/domains/professional/hooks/use-create-profissional";
import {
  registerSchema,
  type RegisterProfessionalFormValues,
} from "@/domains/professional/profissional.schema";
import { ProfessionalFormFields } from "@/domains/professional/components/ProfessionalFormFields";
import { generateAvailabilityMatrix } from "@/domains/professional/shared/disponibilidade.utils";
import { buildProfessionalPayload } from "@/domains/professional/shared/professional.utils";
import { usePhotoUpload } from "@/lib/use-photo-upload";
import { PhotoUpload } from "@/components/shared/PhotoUpload";

export default function ProfessionalRegister(): JSX.Element {
  const router = useRouter();

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
      state: "" as RegisterProfessionalFormValues["state"],
      city: "",
      neighborhood: "",
      street: "",
      noNumber: false,
      number: "",
      complement: "",
      cep: "",
      availability: generateAvailabilityMatrix([]),
    },
  });

  const { create, loading, error, success } = useCreateProfessional(form.setError);

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
            render={({ field, fieldState }) => (
              <FormItem>
                <FormControl>
                  <DocumentUpload
                    id={field.name}
                    label="Termo do Voluntário *"
                    file={field.value instanceof File ? field.value : null}
                    externalError={fieldState.error?.message ?? null}
                    onFileChange={(file) => {
                      field.onChange(file ?? undefined);
                    }}
                    onErrorChange={(err) => {
                      if (err) {
                        form.setError("volunteerAgreement", {
                          type: "manual",
                          message: err,
                        });
                      } else {
                        form.clearErrors("volunteerAgreement");
                      }
                    }}
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="curriculum"
            render={({ field, fieldState }) => (
              <FormItem>
                <FormControl>
                  <DocumentUpload
                    id={field.name}
                    label="Currículo *"
                    file={field.value instanceof File ? field.value : null}
                    externalError={fieldState.error?.message ?? null}
                    onFileChange={(file) => {
                      field.onChange(file ?? undefined);
                    }}
                    onErrorChange={(err) => {
                      if (err) {
                        form.setError("curriculum", {
                          type: "manual",
                          message: err,
                        });
                      } else {
                        form.clearErrors("curriculum");
                      }
                    }}
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="attachmentAny"
            render={({ field, fieldState }) => (
              <FormItem>
                <FormControl>
                  <DocumentUpload
                    id={field.name}
                    label="Anexo qualquer"
                    file={field.value instanceof File ? field.value : null}
                    externalError={fieldState.error?.message ?? null}
                    onFileChange={(file) => {
                      field.onChange(file ?? undefined);
                    }}
                    onErrorChange={(err) => {
                      if (err) {
                        form.setError("attachmentAny", {
                          type: "manual",
                          message: err,
                        });
                      } else {
                        form.clearErrors("attachmentAny");
                      }
                    }}
                  />
                </FormControl>
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

import * as z from "zod";
import {
  cpfSchema,
  nomeCompletoSchema,
  rgSchema,
  telefoneSchema,
} from "@/lib/personal-data.schema";
import { MAX_FILE_SIZE_BYTES, MAX_FILE_SIZE_LABEL } from "@/lib/constants";
import { optionalPhotoSchema, photoFileSchema } from "@/lib/photo.schema";
import { addressSchema } from "@/schemas/shared/address.schema";

const fileSchema = z
  .instanceof(File, { message: "Selecione um arquivo" })
  .refine((file) => file.size > 0, "Arquivo não pode estar vazio")
  .refine(
    (file) =>
      [
        "application/pdf",
        "image/png",
        "image/jpeg",
        "image/jpg",
        "image/webp",
      ].includes(file.type),
    "Apenas imagens ou PDF são permitidos"
  )
  .refine((file) => file.size <= MAX_FILE_SIZE_BYTES, `Arquivo deve ser menor que ${MAX_FILE_SIZE_LABEL}`);

const baseSchema = z.object({
  fullName: nomeCompletoSchema,
  email: z.email("Email inválido"),
  cpf: cpfSchema,
  professionalDocument: z.string().optional().nullable(),
  serviceArea: z.string().min(1, "Selecione uma área"),
  rg: rgSchema,

  ...addressSchema.shape,

  phone: telefoneSchema,

  availability: z
    .array(
      z.object({
        day: z.string(),
        shift: z.string(),
        checked: z.boolean(),
      })
    )
    .min(0),

  photo: optionalPhotoSchema,
});

export const registerSchema = baseSchema.extend({
  photo: photoFileSchema,
  volunteerAgreement: fileSchema,
  curriculum: fileSchema,
  attachmentAny: fileSchema.optional(),
});

export const updateProfessionalSchema = baseSchema;

export type ProfessionalFormValues = z.infer<typeof baseSchema>;
export type RegisterProfessionalFormValues = z.infer<typeof registerSchema>;
export type UpdateProfessionalFormValues = z.infer<typeof updateProfessionalSchema>;

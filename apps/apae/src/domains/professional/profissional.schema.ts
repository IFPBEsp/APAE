import * as z from "zod";
import {
  cpfSchema,
  nomeCompletoSchema,
  rgSchema,
  telefoneSchema,
} from "@/lib/personal-data.schema";
import { optionalPhotoSchema, photoFileSchema } from "@/lib/photo.schema";
import { addressSchema } from "@/schemas/shared/address.schema";
import { documentFileSchema } from "@/schemas/shared/document.schema";

const baseSchema = z.object({
  fullName: nomeCompletoSchema,
  email: z.email("Email inválido"),
  cpf: cpfSchema,
  professionalDocument: z
    .string()
    .trim()
    .refine((val) => val === "" || val.length >= 3, {
      message: "O documento profissional deve ter no mínimo 3 caracteres",
    })
    .refine((val) => val.length <= 50, {
      message: "O documento profissional deve ter no máximo 50 caracteres",
    })
    .optional()
    .nullable(),
  serviceArea: z.string().min(1, "Selecione uma área"),
  rg: rgSchema.or(z.literal("")),

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
  volunteerAgreement: documentFileSchema,
  curriculum: documentFileSchema,
  attachmentAny: documentFileSchema.optional(),
});

export const updateProfessionalSchema = baseSchema;

export type ProfessionalFormValues = z.infer<typeof baseSchema>;
export type RegisterProfessionalFormValues = z.infer<typeof registerSchema>;
export type UpdateProfessionalFormValues = z.infer<typeof updateProfessionalSchema>;

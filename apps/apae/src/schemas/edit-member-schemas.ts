import * as z from "zod";
import { Personal, Address, Additionals, Guardian, Profile } from "../domains/patients/schemas/member-schemas";
import { photoFileSchema } from "@/schemas/shared/photo.schema";

export const EditAdditionals = Additionals.pick({
  vaccines: true,
  allergies: true,
});

/**
 * Na edição, a foto pode ser:
 * - ausente (não alterar);
 * - string (URL da foto já cadastrada);
 * - File (nova foto), validado pelo photoFileSchema compartilhado.
 *
 * Usamos custom + superRefine em vez de z.union([photoFileSchema, z.string()])
 * para que um File inválido mostre a mensagem específica (formato/tamanho)
 * e não o erro genérico de união.
 */
const editPhotoSchema = z
  .custom<File | string>((v) => v instanceof File || typeof v === "string", {
    message: "Selecione uma imagem",
  })
  .superRefine((value, ctx) => {
    if (typeof value === "string") return;

    const result = photoFileSchema.safeParse(value);
    if (!result.success) {
      for (const issue of result.error.issues) {
        ctx.addIssue({ code: "custom", message: issue.message });
      }
    }
  });

export const EditProfile = Profile.extend({
  photo: editPhotoSchema.optional(),
});

export const EditPersonal = Personal;
export const EditAddress = Address;
export const EditGuardian = Guardian;

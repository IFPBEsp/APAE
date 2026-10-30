import * as z from "zod";
import { Personal, Address, Additionals, Guardian, Profile } from "../domains/patients/schemas/member-schemas";
import { photoFileSchema } from "@/schemas/shared/photo.schema";

export const EditAdditionals = Additionals.pick({
  vaccines: true,
  allergies: true,
});

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

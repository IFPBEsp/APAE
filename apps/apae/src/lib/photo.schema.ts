import * as z from "zod";
import { MAX_FILE_SIZE_BYTES, MAX_FILE_SIZE_LABEL } from "@/lib/constants";

export const ALLOWED_IMAGE_TYPES = ["image/png", "image/jpeg", "image/jpg", "image/webp"] as const;
export const ALLOWED_IMAGE_LABEL = "PNG, JPG ou WEBP";

export const photoFileSchema = z
  .instanceof(File, { message: "Selecione uma imagem" })
  .refine((file) => file.size > 0, "O arquivo está vazio")
  .refine((file) => (ALLOWED_IMAGE_TYPES as readonly string[]).includes(file.type), `Formato inválido. Use ${ALLOWED_IMAGE_LABEL}`)
  .refine((file) => file.size <= MAX_FILE_SIZE_BYTES, `A foto excede o limite máximo permitido de ${MAX_FILE_SIZE_LABEL}`);

export const optionalPhotoSchema = photoFileSchema.optional().nullable();

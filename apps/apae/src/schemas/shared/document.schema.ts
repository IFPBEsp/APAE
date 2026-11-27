import * as z from "zod";
import { MAX_FILE_SIZE_BYTES, MAX_FILE_SIZE_LABEL } from "@/lib/constants";

export const ALLOWED_DOCUMENT_TYPES = [
  "application/pdf",
  "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
  "image/png",
  "image/jpeg",
  "image/jpg",
  "image/webp",
] as const;

export const ALLOWED_DOCUMENT_LABEL = "PDF, DOCX, PNG, JPG ou WEBP";
export const DOCUMENT_FORMAT_HINT = `${ALLOWED_DOCUMENT_LABEL} até ${MAX_FILE_SIZE_LABEL}`;
export const ALLOWED_DOCUMENT_ACCEPT = ALLOWED_DOCUMENT_TYPES.join(",");

export const documentFileSchema = z
  .instanceof(File, { message: "Selecione um arquivo" })
  .refine((file) => file.size > 0, "O arquivo está vazio")
  .refine(
    (file) => (ALLOWED_DOCUMENT_TYPES as readonly string[]).includes(file.type),
    `Formato inválido. Use ${ALLOWED_DOCUMENT_LABEL}`,
  )
  .refine(
    (file) => file.size <= MAX_FILE_SIZE_BYTES,
    `O arquivo excede o limite máximo permitido de ${MAX_FILE_SIZE_LABEL}`,
  );

export const optionalDocumentFileSchema = documentFileSchema
  .optional()
  .nullable();

export function validateDocumentFile(file: File): string | null {
  const result = documentFileSchema.safeParse(file);
  return result.success
    ? null
    : (result.error.issues[0]?.message ?? "Arquivo inválido");
}

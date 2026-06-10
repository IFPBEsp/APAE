import { z } from "zod";

export const disorderSchema = z.object({
  id: z.string(),
  name: z.string(),
  hasPatient: z.boolean(),
});

export const createDisorderSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const updateDisorderSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export type CreateDisorderFormData = z.infer<typeof createDisorderSchema>;
export type UpdateDisorderFormData = z.infer<typeof updateDisorderSchema>;

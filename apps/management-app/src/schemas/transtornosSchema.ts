import { z } from "zod";

export const disorderSchema = z.object({
  id: z.string(),
  name: z.string(),
  hasPatient: z.boolean(),
});

export const createDisorderSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const updateDisorderSchema = createDisorderSchema;

export type Disorder = z.infer<typeof disorderSchema>;
export type CreateDisorderDTO = z.infer<typeof createDisorderSchema>;
export type UpdateDisorderDTO = z.infer<typeof updateDisorderSchema>;
import { z } from "zod";

export const disorderSchema = z.object({
  id: z.string(),
  name: z.string(),
  hasPatient: z.boolean(),
});

export const disorderFormSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export type DisorderFormData = z.infer<typeof disorderFormSchema>;
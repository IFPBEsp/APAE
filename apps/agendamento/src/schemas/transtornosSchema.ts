import { z } from "zod";

export const transtornoSchema = z.object({
  id: z.string(),
  name: z.string(),
});

export const createTranstornoSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const updateTranstornoSchema = createTranstornoSchema;

export type Transtorno = z.infer<typeof transtornoSchema>;
export type CreateTranstornoDTO = z.infer<typeof createTranstornoSchema>;
export type UpdateTranstornoDTO = z.infer<typeof updateTranstornoSchema>;
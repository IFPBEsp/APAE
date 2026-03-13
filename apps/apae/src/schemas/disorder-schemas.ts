import { z } from "zod";

export const CreateDisorder = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const UpdateDisorder = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

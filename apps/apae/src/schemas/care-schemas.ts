import { z } from "zod";

export const CreateCare = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const UpdateCare = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

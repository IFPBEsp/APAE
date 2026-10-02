import { z } from "zod";

export const vaccineSchema = z.object({
  id: z.string(),
  name: z.string(),
  hasPatient: z.boolean(),
});

export const vaccineFormSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export type VaccineFormData = z.infer<typeof vaccineFormSchema>;

import { z } from "zod";

export const createVaccineSchema = z.object({
  name: z.string().min(1, "O nome da vacina é obrigatório."),
});

export const updateVaccineSchema = z.object({
  name: z.string().min(1, "O nome da vacina é obrigatório."),
});

export type CreateVaccineFormData = z.infer<typeof createVaccineSchema>;
export type UpdateVaccineFormData = z.infer<typeof updateVaccineSchema>;
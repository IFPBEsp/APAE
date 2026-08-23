import { z } from "zod";

export const vaccineSchema = z.object({
  id: z.string(),
  name: z.string(),
  hasPatient: z.boolean(),
});

const vaccineNameSchema = z
  .string()
  .trim()
  .min(2, "O nome da vacina deve ter entre 2 e 100 caracteres.")
  .max(100, "O nome da vacina deve ter entre 2 e 100 caracteres.");

export const createVaccineSchema = z.object({
  name: vaccineNameSchema,
});

export const updateVaccineSchema = z.object({
  name: vaccineNameSchema,
});

export type CreateVaccineFormData = z.infer<typeof createVaccineSchema>;
export type UpdateVaccineFormData = z.infer<typeof updateVaccineSchema>;
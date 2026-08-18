import { z } from "zod";

export const vaccineSchema = z.object({
    id: z.string(),
    name: z.string(),
    hasPatient: z.boolean(),
});

export const createVaccineSchema = z.object({
    name: z.string().min(1, "O nome é obrigatório."),
});

export const updateVaccineSchema = z.object({
    name: z.string().min(1, "O nome é obrigatório."),
});

export type CreateVaccineFormData = z.infer<typeof createVaccineSchema>;
export type UpdateVaccineFormData = z.infer<typeof updateVaccineSchema>;
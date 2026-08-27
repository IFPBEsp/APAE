import { z } from "zod";

export const vaccineSchema = z.object({
    id: z.string(),
    name: z.string(),
    hasPatient: z.boolean(),
});

export const createVaccineSchema = z.object({
    name: z
        .string()
        .min(1, "O nome é obrigatório.")
        .min(2, "O nome da vacina deve ter entre 2 e 100 caracteres.")
        .max(100, "O nome da vacina deve ter entre 2 e 100 caracteres."),
});

export const updateVaccineSchema = z.object({
    name: z
        .string()
        .min(1, "O nome é obrigatório.")
        .min(2, "O nome da vacina deve ter entre 2 e 100 caracteres.")
        .max(100, "O nome da vacina deve ter entre 2 e 100 caracteres."),
});

export type CreateVaccineFormData = z.infer<typeof createVaccineSchema>;
export type UpdateVaccineFormData = z.infer<typeof updateVaccineSchema>;
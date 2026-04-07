import { z } from "zod";

export const vaccineSchema = z.object({
    id: z.string(),
    name: z.string(),
    hasPatient: z.boolean(),

})
export const CreateVaccine = z.object({
    name: z.string().min(1, "O nome é obrigatório."),
});

export const UpdateVaccine = z.object({
    name: z.string().min(1, "O nome é obrigatório."),
});

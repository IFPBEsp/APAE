import { z } from "zod";

export const serviceTypeSchema = z.object({
  id: z.string(),
  area: z.string(),
});

export const createserviceTypeSchema = z.object({
  area: z.string().min(1, "A área é obrigatória."),
});

export const updateserviceTypeSchema = createserviceTypeSchema;

export type ServiceType = z.infer<typeof serviceTypeSchema>;
export type CreateserviceTypeDTO = z.infer<typeof createserviceTypeSchema>;
export type UpdateserviceTypeDTO = z.infer<typeof updateserviceTypeSchema>;
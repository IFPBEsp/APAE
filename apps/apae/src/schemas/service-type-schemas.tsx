import { z } from "zod";

export const serviceTypeSchema = z.object({
  id: z.string(),
  name: z.string(),
});

export const createserviceTypeSchema = z.object({
  name: z.string().min(1, "O nome é obrigatório."),
});

export const updateserviceTypeSchema = createserviceTypeSchema;

export type ServiceType = z.infer<typeof serviceTypeSchema>;
export type CreateserviceTypeDTO = z.infer<typeof createserviceTypeSchema>;
export type UpdateserviceTypeDTO = z.infer<typeof updateserviceTypeSchema>;

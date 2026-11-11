import { z } from "zod";
export type { ServiceType } from "./service-types.types";

export const serviceTypeSchema = z.object({
  id: z.number(),
  area: z.string(),
});

export const createServiceTypeSchema = z.object({
  area: z.string().min(1, "A área é obrigatória."),
});

export const updateServiceTypeSchema = createServiceTypeSchema;

export type CreateServiceTypeDTO = z.infer<typeof createServiceTypeSchema>;
export type UpdateServiceTypeDTO = z.infer<typeof updateServiceTypeSchema>;

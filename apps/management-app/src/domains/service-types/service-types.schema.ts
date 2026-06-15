import { z } from "zod";

export const serviceTypeSchema = z.object({
  id: z.union([z.string(), z.number()]),
  name: z.string(),
});

export const createServiceTypeSchema = z.object({
  name: z.string().min(1, "O nome e obrigatorio."),
});

export const updateServiceTypeSchema = createServiceTypeSchema;

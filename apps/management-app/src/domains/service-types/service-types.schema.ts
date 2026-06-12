import { z } from "zod";

export const serviceTypeSchema = z.object({
  id: z.union([z.string(), z.number()]),
  area: z.string(),
});

export const createServiceTypeSchema = z.object({
  area: z.string().min(1, "A area e obrigatoria."),
});

export const updateServiceTypeSchema = createServiceTypeSchema;

// src/schemas/annualRegistrySchema.ts

import { z } from "zod";

export const AnnualRegistryFormSchema = z.object({
  bpc: z.boolean(), 
  familyIncome: z.string().min(1, "Renda Familiar é obrigatória"), 
  diseases: z.string().optional(),
  continuousMedication: z.string().optional(), 
  disorders: z.string().optional(),
});

export type AnnualRegistryFormData = z.infer<typeof AnnualRegistryFormSchema>;
import { z } from "zod";

export const AnnualRegistryFormSchema = z.object({
  bpc: z.any().transform((val) => {
    const s = String(val).toLowerCase();
    return s === "true" || s === "sim";
  }),

  familyIncome: z.string().default(""),
  diseases: z.string().optional().default(""),
  continuousMedication: z.string().optional().default(""),
  disorders: z.array(z.object({ 
    name: z.string(),
    id: z.string().optional()
  })).optional().default([]),
  allergies: z.any().optional(),
  vaccines: z.any().optional() 
});

export type AnnualRegistryFormData = z.infer<typeof AnnualRegistryFormSchema>;
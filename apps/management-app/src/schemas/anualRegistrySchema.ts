import { z } from "zod";

const GenericItemSchema = z.object({
  id: z.union([z.string(), z.number()]).optional(),
  name: z.string().optional(),
  area: z.string().optional(),
});

export const AnnualRegistryFormSchema = z.object({
  bpc: z.preprocess((val) => {
    if (typeof val === "string") return val === "true";
    return val;
  }, z.boolean()),
  
  familyIncome: z.string(),
  diseases: z.string().optional(),
  allergies: z.string().optional(),
  continuousMedication: z.string().optional(),
  vaccines: z.array(GenericItemSchema).optional(),
  disorders: z.array(GenericItemSchema).optional(),
  serviceTypes: z.array(GenericItemSchema).optional(),
});

export type AnnualRegistryFormData = z.infer<typeof AnnualRegistryFormSchema>;
import { z } from "zod";

export interface AnnualRegistryFormValues {
  year: string;
  bpc: string;
  familyIncome: string;
  diseases: string;
  allergies: string;
  continuousMedication: string;
  vaccines: { id?: string | number; name?: string; area?: string }[];
  disorders: { id?: string | number; name?: string; area?: string }[];
  serviceTypes: { id?: string | number; name?: string; area?: string }[];
}

const GenericItemSchema = z.object({
  id: z.union([z.string(), z.number()]).optional(),
  name: z.string().optional(),
  area: z.string().optional(),
});

export const AnnualRegistryFormSchema = z.object({
  year: z.string().min(4, "Selecione um ano"),
  bpc: z.string(),
  familyIncome: z.string(),
  diseases: z.string(),
  allergies: z.string(),
  continuousMedication: z.string(),
  vaccines: z.array(GenericItemSchema),
  disorders: z.array(GenericItemSchema),
  serviceTypes: z.array(GenericItemSchema),
});
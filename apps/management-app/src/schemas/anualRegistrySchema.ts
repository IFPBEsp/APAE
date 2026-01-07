// src/schemas/anualRegistrySchema.ts
import { z } from "zod";

export const AnnualRegistryFormSchema = z.object({
  // Validação estrita: não aceita vazio
  bpc: z.string().min(1, "Informe se recebe BPC"), 
  
  familyIncome: z.string().min(1, "Renda familiar é obrigatória"),
  
  // Z.string() puro aceita string vazia "" mas não aceita undefined/null
  // Isso satisfaz o React Hook Form
  diseases: z.string(),
  
  continuousMedication: z.string(),
  
  // Garante que é um array, mesmo que vazio
  disorders: z.array(z.object({ 
    name: z.string(),
    id: z.string().optional() // Aceita ID se vier do banco
  })),
  
  // Campos do PACIENTE
  allergies: z.string(),
  vaccines: z.string() 
});

export type AnnualRegistryFormData = z.infer<typeof AnnualRegistryFormSchema>;
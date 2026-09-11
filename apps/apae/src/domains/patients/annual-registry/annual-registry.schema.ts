import * as z from "zod";

export const annualRegistryFormSchema = z.object({
  year: z.string().min(4, "Ano inválido"),
  bpc: z.enum(["true", "false"]),
  familyIncome: z.string().min(1, "A renda é obrigatória").refine((val) => {
    const num = parseFloat(val.replace(/[^\d]/g, ""));
    return num >= 20000;
  }, "Mínimo R$ 200,00"),
  diseases: z.string().min(1, "Informe as doenças ou 'Nenhuma'"),
  continuousMedication: z.string().min(1, "Informe os medicamentos ou 'Nenhum'"),
  allergies: z.string().min(1, "Informe as alergias ou 'Nenhuma'"),
  vaccines: z.array(z.any()).min(1, "Selecione ao menos uma vacina"),
  disorders: z.array(z.any()).min(1, "Selecione ao menos um transtorno"),
  serviceTypes: z.array(z.any()).min(1, "Selecione ao menos um atendimento"),
});

export type AnnualRegistryFormValues = z.infer<typeof annualRegistryFormSchema>;

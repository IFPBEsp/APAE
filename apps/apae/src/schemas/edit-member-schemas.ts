import z from "zod";

export const EditPersonal = z.object({
  name: z.string().min(2, "Nome é obrigatório"),
  cpf: z.string().min(1, "CPF é obrigatório"),
  phone: z.string().min(1, "Telefone é obrigatório"),
  rg: z.object({
    number: z.string().min(1, "RG é obrigatório"),
    issuing: z.object({
      body: z.string().min(1, "Órgão emissor é obrigatório"),
      date: z.coerce.date() as any, 
    }),
  }),
  cns: z.string().optional().or(z.literal("")),
  nis: z.string().optional().or(z.literal("")),
  birth: z.object({
    certificate: z.string().min(1, "Obrigatório"),
    date: z.coerce.date() as any,
    place: z.string().min(1, "Obrigatório"),
  }),
});

export const EditAddress = z.object({
  cep: z.string().min(1, "CEP é obrigatório"),
  state: z.string().min(1, "Obrigatório"),
  city: z.string().min(1, "Obrigatório"),
  district: z.string().min(1, "Obrigatório"),
  street: z.string().min(1, "Obrigatório"),
});

export const EditAdditionals = z.object({
  diseases: z.string().optional(),
  medications: z.string().optional(),
  vaccines: z.array(z.string()).optional(),
  allergies: z.string().optional(),
  bpc: z.boolean().optional(),
  householdIncome: z.string().optional(),
  disability: z.object({ types: z.array(z.string()) }).optional(),
  care: z.object({ types: z.array(z.string()) }).optional(),
});

export const EditProfile = z.object({
  role: z.enum(["student", "patient"], {
    message: "Tipo de eficiência é obrigatório.",
  }),
  photo: z.any().optional(), 
});
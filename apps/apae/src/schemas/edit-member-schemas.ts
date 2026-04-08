import z, { number } from "zod";

// 1. Endereço relaxado 
export const EditAddress = z.object({
  cep: z.string().min(1, "CEP é obrigatório"),
  state: z.string().min(1, "Obrigatório"),
  city: z.string().min(1, "Obrigatório"),
  district: z.string().min(1, "Obrigatório"),
  street: z.string().min(1, "Obrigatório"),
  noNumber: z.boolean().optional(),
  number: z.string().min(1, "Número é obrigatório").regex(/^(\d+|SN|sn|S\/N|s\/n)$/, "Número deve conter apenas dígitos ou SN"),
  complement: z.string().optional(),
});

// 2. Dados Pessoais relaxados
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
  nis: z
    .string()
    .min(1, "NIS é obrigatório")
    .length(11, "O NIS deve ter exatamente 11 dígitos")
    .regex(/^\d+$/, "O NIS deve conter apenas números"),
  birth: z.object({
    certificate: z.string().min(1, "Obrigatório"),
    date: z.coerce.date() as any,
    place: z.string().min(1, "Obrigatório"),
  }),
});

// 3. Adicionais relaxados
export const EditAdditionals = z.object({
  diseases: z.string().optional(),
  medications: z.string().optional(),
  vaccines: z.array(z.string()).optional(),
  allergies: z.string().optional(),
  bpc: z.boolean().optional(),
  householdIncome: z.string().optional(),
  disability: z.object({ types: z.array(z.string()).optional(), report: z.any().optional() }).optional(),
  care: z.object({ types: z.array(z.string()).optional(), referral: z.any().optional() }).optional(),
});

// 4. RESPONSÁVEL 
export const EditGuardian = z.object({
  name: z.string().min(2, "Nome é obrigatório"),
  contact: z.string().min(1, "Contato é obrigatório"),
  kinship: z.string().min(1, "Parentesco é obrigatório"),
  address: EditAddress, 
});

// 5. Perfil relaxado
export const EditProfile = z.object({
  role: z.enum(["student", "patient"]),
  photo: z.any().optional(),
});
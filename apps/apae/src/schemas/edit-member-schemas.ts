import z from "zod";

// 1. Endereço relaxado 
export const EditAddress = z.object({
  cep: z.string().min(1, "CEP é obrigatório"),
  state: z.string().min(1, "Obrigatório"),
  city: z.string().min(1, "Obrigatório"),
  district: z.string().min(1, "Obrigatório"),
  street: z.string().min(1, "Obrigatório"),
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
      date: z.coerce.date(), 
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
    date: z.coerce.date(),
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
  disability: z.object({ types: z.array(z.string()).optional(), report: z.union([z.instanceof(File), z.string()]).optional() }).optional(),
  care: z.object({ types: z.array(z.string()).optional(), referral: z.union([z.instanceof(File), z.string()]).optional() }).optional(),
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
  photo: z.union([z.instanceof(File), z.string()]).optional(),
});

export type EditAddressData = z.infer<typeof EditAddress>;
export type EditPersonalData = z.infer<typeof EditPersonal>;
export type EditAdditionalsData = z.infer<typeof EditAdditionals>;
export type EditGuardianData = z.infer<typeof EditGuardian>;
export type EditProfileData = z.infer<typeof EditProfile>;
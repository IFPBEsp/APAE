import * as z from "zod";

const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$/;
const telefoneRegex = /^\(\d{2}\) \d{5}-\d{4}$/;
const cepRegex = /^\d{5}-\d{3}$/;
const cpfRegex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;

const rgRegex =
  /^(\d{7,8}[\dXx]?|\d\.\d{3}\.\d{3}|\d{2}\.\d{3}\.\d{3}-[\dXx])$/;

const fileSchema = z
  .instanceof(File, { message: "Selecione um arquivo" })
  .refine((file) => file.size > 0, "Arquivo não pode estar vazio")
  .refine(
    (file) =>
      [
        "application/pdf",
        "image/png",
        "image/jpeg",
        "image/jpg",
        "image/webp",
      ].includes(file.type),
    "Apenas imagens ou PDF são permitidos"
  )
  .refine((file) => file.size <= 5 * 1024 * 1024, "Arquivo deve ser menor que 5MB");

  const imageSchema = z
  .instanceof(File)
  .refine((file) => file.size <= 2 * 1024 * 1024, "A foto deve ser menor que 2MB")
  .refine(
    (file) => ["image/jpeg", "image/png", "image/jpg", "image/webp"].includes(file.type),
    "Apenas formatos JPG, PNG ou WEBP são aceitos"
  );

const baseSchema = z.object({
  nomeCompleto: z
    .string()
    .regex(
      nomeRegex,
      "Nome inválido. Use apenas letras e espaços, 3-100 caracteres"
    ),

  email: z.email("Email inválido"),

  cpf: z.string().regex(cpfRegex, "CPF inválido. Formato esperado: 999.999.999-99"),

  documentoProfissional: z.string().optional().nullable(),

  areaAtendimento: z.string().min(1, "Selecione uma área"),

  rg: z
    .string()
    .regex(rgRegex, "RG inválido")
    .transform((val) => val.replace(/\W/g, "")),

  telefone: z
    .string()
    .regex(
      telefoneRegex,
      "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx"
    )
    .transform((val) => val.trim()),

  address: z.object({
    cep: z.string().regex(cepRegex, "CEP inválido. Formato esperado: 99999-999"),
    state: z.string().min(1, "Selecione um estado"),
    city: z.string().min(1, "Cidade é obrigatória"),
    neighborhood: z.string().min(2, "Bairro é obrigatório"),
    street: z.string().min(2, "Rua é obrigatória"),
    number: z.string().min(1, "Número é obrigatório").max(20, "Número deve ter no máximo 20 caracteres"),
    complement: z.string().max(60, "Complemento deve ter no máximo 60 caracteres").optional().nullable(),
  }),

  disponibilidade: z
    .array(
      z.object({
        dia: z.string(),
        turno: z.string(),
        checked: z.boolean(),
      })
    )
    .min(0),

  photo: imageSchema.optional().nullable(),
});

export const cadastroSchema = baseSchema.extend({
  termoVoluntariado: fileSchema,
  curriculo: fileSchema,
  anexoQualquer: fileSchema.optional(),
});

export const updateProfessionalSchema = baseSchema;

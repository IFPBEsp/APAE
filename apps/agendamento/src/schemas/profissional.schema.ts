import * as z from "zod";

const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$/;
const telefoneRegex = /^\(\d{2}\) \d{5}-\d{4}$/;
const docProfissionalRegex =
  /^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\s\d{2})?\s?\d{1,6}$/;
const rgRegex =
  /^(\d{7,8}[\dXx]?|\d\.\d{3}\.\d{3}|\d{2}\.\d{3}\.\d{3}-[\dXx])$/;
const estadoRegex = /^[A-Z]{2}$/;
const cidadeRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const bairroRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const ruaRegex = /^[A-Za-zÀ-ÿ0-9\s]+$/;
const numeroRegex = /^[0-9]{1,6}$/;

const fileSchema = z
  .instanceof(File, { message: "Selecione um arquivo" })
  .refine((file) => file.size > 0, "Arquivo não pode estar vazio");

const baseSchema = z.object({
  nomeCompleto: z
    .string()
    .regex(
      nomeRegex,
      "Nome inválido. Use apenas letras e espaços, 3-100 caracteres"
    ),

  email: z.email("Email inválido"),

  documentoProfissional: z
    .string()
    .regex(docProfissionalRegex, "Documento profissional inválido"),

  areaAtendimento: z.string().min(1, "Selecione uma área"),

  rg: z
    .string()
    .regex(rgRegex, "RG inválido")
    .transform((val) => val.replace(/\W/g, "")),

  estado: z.string().regex(estadoRegex, "Estado inválido"),
  cidade: z.string().regex(cidadeRegex, "Cidade inválida"),
  bairro: z.string().regex(bairroRegex, "Bairro inválido"),
  rua: z.string().regex(ruaRegex, "Rua inválida"),
  numero: z.string().regex(numeroRegex, "Número inválido"),

  complemento: z.string().optional(),

  cep: z
    .string()
    .regex(/^\d{5}-\d{3}$/, "CEP inválido. Formato esperado: XXXXX-XXX"),

  telefone: z
    .string()
    .regex(
      telefoneRegex,
      "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx"
    )
    .transform((val) => val.trim()),

  disponibilidade: z
    .array(
      z.object({
        dia: z.string(),
        turno: z.string(),
        checked: z.boolean(),
      })
    )
    .default([])
    .optional(),
});

export const cadastroSchema = baseSchema.extend({
  termoVoluntariado: fileSchema,
  curriculo: fileSchema,
  anexoQualquer: fileSchema.optional(),
});

export const updateProfessionalSchema = baseSchema;

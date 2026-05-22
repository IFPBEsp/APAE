import * as z from "zod";

const nameRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$/;
const phoneRegex = /^\(\d{2}\) \d{5}-\d{4}$/;

const rgRegex =
  /^(\d{7,8}[\dXx]?|\d\.\d{3}\.\d{3}|\d{2}\.\d{3}\.\d{3}-[\dXx])$/;
const stateRegex = /^[A-Z]{2}$/;
const cityRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const neighborhoodRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const streetRegex = /^[A-Za-zÀ-ÿ0-9\s]+$/;
const numberRegex = /^[0-9]{1,6}$/;

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
  fullName: z
    .string()
    .regex(
      nameRegex,
      "Nome inválido. Use apenas letras e espaços, 3-100 caracteres"
    ),

  email: z.email("Email inválido"),

  professionalDocument: z.string().optional().nullable(),

  serviceArea: z.string().min(1, "Selecione uma área"),

  rg: z
    .string()
    .regex(rgRegex, "RG inválido")
    .transform((val) => val.replace(/\W/g, "")),

  state: z.string().regex(stateRegex, "Estado inválido"),
  city: z.string().regex(cityRegex, "Cidade inválida"),
  neighborhood: z.string().regex(neighborhoodRegex, "Bairro inválido"),
  street: z.string().regex(streetRegex, "Rua inválida"),
  number: z.string().regex(numberRegex, "Número inválido"),

  complement: z.string().optional(),

  cep: z
    .string()
    .regex(/^\d{5}-\d{3}$/, "CEP inválido. Formato esperado: XXXXX-XXX"),

  phone: z
    .string()
    .regex(
      phoneRegex,
      "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx"
    )
    .transform((val) => val.trim()),

  availability: z
    .array(
      z.object({
        day: z.string(),
        shift: z.string(),
        checked: z.boolean(),
      })
    )
    .min(0),

  photo: imageSchema.optional().nullable(),
});

export const registerSchema = baseSchema.extend({
  termoVoluntariado: fileSchema,
  curriculo: fileSchema,
  anexoQualquer: fileSchema.optional(),
});

export const updateProfessionalSchema = baseSchema;

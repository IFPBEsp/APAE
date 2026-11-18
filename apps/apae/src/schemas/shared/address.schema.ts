import * as z from "zod";
import { STATES } from "@/lib/states";

export const cepRegex = /^\d{5}-\d{3}$/;
export const cityRegex = /^[\p{L}\s'’-]+$/u;
export const neighborhoodRegex = /^[\p{L}0-9\s'’.,()-]+$/u;
export const streetRegex = /^[\p{L}0-9\s'’.,()ºª°/#-]+$/u;
export const numberRegex = /^[0-9A-Za-z/-]{1,10}$/;
export const complementRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ0-9\s'°º.,/#()-]*$/;

export const addressSchema = z.object({
  cep: z
    .string()
    .min(1, "O CEP não pode estar em branco")
    .regex(cepRegex, "O CEP deve estar no formato 99999-999"),
  state: z.enum(STATES, {
    message: "Selecione um estado válido",
  }),
  city: z
    .string()
    .min(2, "A cidade deve ter entre 2 e 60 caracteres")
    .max(60, "A cidade deve ter entre 2 e 60 caracteres")
    .regex(
      cityRegex,
      "A cidade deve conter apenas letras, espaços, apóstrofo ou hífen",
    ),
  neighborhood: z
    .string()
    .min(2, "O bairro deve ter entre 2 e 60 caracteres")
    .max(60, "O bairro deve ter entre 2 e 60 caracteres")
    .regex(
      neighborhoodRegex,
      "O bairro contém caracteres inválidos",
    ),
  street: z
    .string()
    .min(2, "A rua deve ter entre 2 e 80 caracteres")
    .max(80, "A rua deve ter entre 2 e 80 caracteres")
    .regex(
      streetRegex,
      "A rua contém caracteres inválidos",
    ),
  noNumber: z.boolean().optional(),
  number: z
    .string()
    .min(1, "O número não pode estar em branco")
    .regex(
      numberRegex,
      "O número deve conter apenas letras, números, / ou -",
    ),
  complement: z
    .string()
    .max(60, "O complemento deve ter no máximo 60 caracteres")
    .regex(
      complementRegex,
      "O complemento contém caracteres inválidos",
    )
    .optional(),
});

export type AddressSchemaValues = z.infer<typeof addressSchema>;

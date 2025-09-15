import * as z from "zod";
import { HEALTH_AREAS } from "@/lib/health-areas";

const nomeRegex = /^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$/;
const telefoneRegex = /^\(\d{2}\) \d{5}-\d{4}$/;
const docProfissionalRegex =
  /^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\s\d{2})?\s?\d{1,6}$/;
const rgRegex =
  /^(\d\.\d{3}\.\d{3}|\d{7,8}[\dXx]?|\d{2}\.\d{3}\.\d{3}-[\dXx])$/;
const estadoRegex = /^[A-Z]{2}$/;
const cidadeRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const bairroRegex = /^[A-Za-zÀ-ÿ\s]+$/;
const ruaRegex = /^[A-Za-zÀ-ÿ0-9\s]+$/;
const numeroRegex = /^[0-9]{1,6}$/;

export const cadastroSchema = z.object({
  nomeCompleto: z
    .string()
    .regex(
      nomeRegex,
      "Nome inválido. Use apenas letras e espaços, 3-100 caracteres"
    ),
  email: z.string().email("Email inválido"),
  documentoProfissional: z
    .string()
    .regex(docProfissionalRegex, "Documento profissional inválido"),
  areaSaude: z
    .string()
    .min(1, "Selecione uma área válida")
    .refine((v) => (HEALTH_AREAS as readonly string[]).includes(v), {
      message: "Selecione uma área válida",
    }),
  rg: z.preprocess(
    (val) => (typeof val === "string" ? val.replace(/\W/g, "") : val),
    z.string().regex(rgRegex, "RG inválido")
  ),
  estado: z.string().regex(estadoRegex, "Estado inválido"),
  cidade: z.string().regex(cidadeRegex, "Cidade inválida"),
  bairro: z.string().regex(bairroRegex, "Bairro inválido"),
  rua: z.string().regex(ruaRegex, "Rua inválida"),
  numero: z.string().regex(numeroRegex, "Número inválido"),
  complemento: z.string().optional(),
  cep: z
    .string()
    .regex(/^\d{5}-\d{3}$/, "CEP inválido. Formato esperado: XXXXX-XXX"),
  telefone: z.preprocess(
    (val) => (typeof val === "string" ? val.trim() : val),
    z
      .string()
      .regex(
        telefoneRegex,
        "Telefone inválido. Formato esperado: (xx) xxxxx-xxxx"
      )
  ),
});

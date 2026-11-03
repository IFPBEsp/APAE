import * as z from "zod";
import { isValidCpf } from "@/lib/validators/cpf";

export const cpfSchema = z
  .string()
  .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF inválido")
  .refine(isValidCpf, {
    message: "CPF inválido",
  });

export const nomeCompletoSchema = z
  .string()
  .trim()
  .min(1, "Nome obrigatório")
  .max(100, "O nome deve ter no máximo 100 caracteres")
  .regex(
    /^[\p{L}]+(?: \p{L}+)+$/u,
    "Por favor, insira o nome e sobrenome"
  );

export const rgSchema = z
  .string()
  .trim()
  .regex(
    /^\d+(?:\.\d+)*(?:-[\dX])?$/,
    "RG inválido"
  );

export const telefoneSchema = z
  .string()
  .trim()
  .regex(
    /^\(\d{2}\) (?:9\d{4}|\d{4})-\d{4}$/,
    "Número de telefone inválido"
  );

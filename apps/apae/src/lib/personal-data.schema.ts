import * as z from "zod";
import { isValidCpf } from "@/lib/validators/cpf";

export const cpfSchema = z
  .string()
  .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF inválido")
  .refine(isValidCpf, {
    message: "CPF inválido",
  });
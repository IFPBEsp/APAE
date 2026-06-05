import z from "zod";

const cpfRegex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;
const cpfSchema = z
  .string()
  .trim()
  .regex(cpfRegex, { message: "CPF deve estar no formato XXX.XXX.XXX-XX" });

export const patientSchema = z.object({
  id: z.string().uuid(),
  fullName: z.string().optional(),
  isStudent: z.boolean().optional(),
  name: z.string().min(1, "Nome é obrigatório").optional(),
  cpf: cpfSchema,
  status: z.enum(["Ativo", "Inativo", "Em Fila"]),
  urlPhoto: z.string().url({ message: "URL da foto inválida" }).optional(),
  contact: z.object({
    phone: z.string().min(10, { message: "Telefone inválido" }),
  }),
  city: z.string().optional(),
});

export type Patient = z.infer<typeof patientSchema>;

export const signUpSchema = z
  .object({
    fullName: z.string().min(1, {
      message: "Nome completo é obrigatório",
    }),
    email: z
      .string()
      .email({ message: "Email inválido" })
      .min(1, { message: "Email é obrigatório" }),
    cpf: cpfSchema,
    password: z
      .string()
      .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
    confirmPassword: z
      .string()
      .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
  })
  .refine((data) => data.password === data.confirmPassword, {
    message: "As senhas não coincidem",
    path: ["confirmarSenha"],
  });

export type FormSignUp = z.infer<typeof signUpSchema>;

export const loginSchema = z.object({
  username: z
    .string()
    .trim()
    .refine(
      (value) => {
        const isEmail = z.email().safeParse(value).success;
        const isCpf = cpfSchema.safeParse(value).success;
        return isEmail || isCpf;
      },
      {
        message: "Digite um email ou CPF válido",
      },
    ),
  password: z
    .string()
    .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
});

export type FormLogin = z.infer<typeof loginSchema>;

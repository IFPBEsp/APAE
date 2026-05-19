import z from "zod";

const cpfRegex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;
const cpfSchema = z
  .string()
  .trim()
  .regex(cpfRegex, { message: "CPF deve estar no formato XXX.XXX.XXX-XX" });

export const patientSchema = z.object({
  id: z.string().uuid(),
  nome: z.string().min(1, "Nome é obrigatório").optional(),
  cpf: cpfSchema,
  status: z.enum(["Ativo", "Inativo", "Em Fila"]),
  urlFoto: z.string().url({ message: "URL da foto inválida" }).optional(),
  contato: z.object({
    telefone: z.string().min(10, { message: "Telefone inválido" }),
  }),
  cidade: z.string().optional(),
});

export type Patient = z.infer<typeof patientSchema>;

export const signUpSchema = z
  .object({
    nomeCompleto: z.string().min(1, {
      message: "Nome completo é obrigatório",
    }),
    email: z
      .string()
      .email({ message: "Email inválido" })
      .min(1, { message: "Email é obrigatório" }),
    cpf: cpfSchema,
    senha: z
      .string()
      .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
    confirmarSenha: z
      .string()
      .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
  })
  .refine((data) => data.senha === data.confirmarSenha, {
    message: "As senhas não coincidem",
    path: ["confirmarSenha"],
  });
''
export type FormSignUp = z.infer<typeof signUpSchema>;

export const loginSchema = z.object({
  username: z
    .string()
    .trim()
    .refine(
      (value) => {
        const isEmail = z.string().email().safeParse(value).success;
        const isCpf = cpfSchema.safeParse(value).success;

        return isEmail || isCpf;
      },
      {
        message: "Digite um email ou CPF válido.",
      }
    ),
  password: z
    .string()
    .min(6, { message: "Senha deve ter pelo menos 6 caracteres." }),
});

export const recoverySchema = z.object({
  email: z
    .string()
    .trim()
    .min(1, "E-mail é obrigatório")
    .email("Email inválido"),
});

export type FormLogin = z.infer<typeof loginSchema>;

export const newPasswordSchema = z.object({
  senha: z.string().min(6, "A senha deve ter pelo menos 6 caracteres"),
  confirmarSenha: z.string(),
}).refine((data) => data.senha === data.confirmarSenha, {
  message: "As senhas não coincidem",
  path: ["confirmarSenha"],
});

export type FormNewPasswordSchema = z.infer<typeof newPasswordSchema>;

export type FormRecovery = z.infer<typeof recoverySchema>;
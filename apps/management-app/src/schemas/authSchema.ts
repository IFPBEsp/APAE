import z from "zod";

export const signUpSchema = z
  .object({
    nomeCompleto: z.string().min(1, {
      message: "Nome completo é obrigatório",
    }),
    email: z
      .email({ message: "Email inválido" })
      .min(1, { message: "Email é obrigatório" }),
    cpf: z
      .string()
      .trim()
      .min(14, { message: "CPF deve ser formatado por XXX.XXX.XXX-XX" })
      .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/),
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

export type FormSignUp = z.infer<typeof signUpSchema>;

const cpfRegex = /^\d{3}\.\d{3}\.\d{3}-\d{2}$/;

export const loginSchema = z.object({
  username: z
    .string()
    .trim()
    .refine(
      (value) => {
        const isEmail = z.email().safeParse(value).success;
        const isCpf = cpfRegex.test(value);
        return isEmail || isCpf;
      },
      {
        message: "Digite um CPF (XXX.XXX.XXX-XX) ou email válido",
      }
    ),
  password: z
    .string()
    .min(6, { message: "Senha deve ter pelo menos 6 caracteres" }),
});

export type FormLogin = z.infer<typeof loginSchema>;

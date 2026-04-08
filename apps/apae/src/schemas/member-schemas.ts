import z, { number, ZodCoercedDate } from "zod";

const CPF = z
  .string()
  .min(1, "CPF é obrigatório")
  .min(14, "CPF deve ter pelo menos 11 dígitos")
  .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "Formato de CPF inválido");

const RG = z
  .string()
  .min(1, "RG é obrigatório")
  .min(9, "RG deve ter pelo menos 7 dígitos")
  .regex(/^\d\.\d{3}\.\d{3}$/, "Formato de RG inválido");

const CNS = z
  .string()
  .min(1, "CNS é obrigatório")
  .min(18, "CNS deve ter pelo menos 18 dígitos")
  .regex(/^\d{3} \d{4} \d{4} \d{4}$/, "Formato de CNS inválido");

const NIS = z
  .string()
  .min(1, "NIS é obrigatório")
  .length(11, "O NIS deve ter exatamente 11 dígitos") 
  .regex(/^\d+$/, "O NIS deve conter apenas números");

  export const Personal = z
  .object({
    name: z
      .string()
      .transform((v) => v.trim())
      .pipe(
        z
          .string()
          .min(2, "Nome muito curto")
          .max(100, "Nome muito longo")
          .regex(/^[a-zA-ZÀ-ÿ\s]+$/, "Nome deve conter apenas letras e espaços")
          .refine((val) => val.split(/\s+/).length >= 2, {
            message: "Digite o nome completo (nome e sobrenome)",
          }),
      ),
    cpf: CPF,
    phone: z
      .string()
      .min(1, "Telefone é obrigatório")
      .min(14, "Telefone deve ter pelo menos 10 dígitos"),
    rg: z.object({
      number: RG,
      issuing: z.object({
        body: z
          .string()
          .min(1, "Órgão emissor é obrigatório")
          .min(2, "Órgão emissor inválido")
          .max(10, "Órgão emissor muito longo")
          .regex(
            /^[A-Z]{2,4}\/[A-Z]{2}$/,
            "Formato inválido. Use SSP/UF, PC/UF, etc.",
          ),
        date: z.coerce
          .date({ error: "Data de emissão inválida" })
          .refine((date) => date <= new Date(), {
            message: "Data de emissão não pode ser futura",
          })
          .refine(
            (date) => {
              const minDate = new Date();
              minDate.setFullYear(minDate.getFullYear() - 50);
              return date >= minDate;
            },
            {
              message: "Data de emissão muito antiga",
            },
          ) as ZodCoercedDate<Date>,
      }),
    }),
    cns: CNS,
    nis: NIS,
    birth: z.object({
      certificate: z
        .string()
        .min(1, "Certidão de nascimento é obrigatória")
        .min(40, "Número da certidão deve ter pelo menos 32 dígitos"),
      date: z.coerce
        .date({ error: "Data de nascimento inválida" })
        .refine((date) => date <= new Date(), {
          message: "Data de nascimento não pode ser futura",
        })
        .refine(
          (date) => {
            const minDate = new Date();
            minDate.setFullYear(minDate.getFullYear() - 150);
            return date >= minDate;
          },
          {
            message: "Data de nascimento inválida",
          },
        )
        .refine(
          (date) => {
            const today = new Date();
            const age = today.getFullYear() - date.getFullYear();
            return age >= 0;
          },
          {
            message: "Idade deve ser positiva",
          },
        ) as ZodCoercedDate<Date>,
      place: z.string().min(1, "Naturalidade é obrigatória."),
    }),
  })
  .refine((data) => data.rg.issuing.date > data.birth.date, {
    message: "A data de emissão do RG não pode ser anterior ao nascimento",
    path: ["rg", "issuing", "date"],
  });

export const Address = z.object({
  cep: z.string().min(1, "CEP é obrigatório.").min(9, "CEP deve ter 8 dígitos"),
  state: z
    .string()
    .min(3, "Estado deve ser o nome completo (ex: Paraíba)")
    .regex(/^[a-zA-ZÀ-ÿ\s]+$/, "O estado deve conter apenas letras"),
  city: z.string().min(2, "Cidade inválida"),
  district: z.string().min(2, "Bairro inválido"),
  street: z
    .string()
    .min(2, "Rua é obrigatória")
    .regex(/^[a-zA-ZÀ-ÿ\s\.\-,]+$/,
      "Informe a rua (ex: Rua tal)",
    ),
    noNumber: z.boolean().optional(),
    number: z.string().min(1, "Número é obrigatório").regex(/^(\d+|SN|sn|S\/N|s\/n)$/, "Número deve conter apenas dígitos ou SN"),
    complement: z.string().optional(),
});

export const Additionals = z.object({
  diseases: z.string().min(1, "O campo de doenças é obrigatório."),
  medications: z.string().min(1, "O campo de medicações é obrigatório."),
  vaccines: z
    .array(z.string().min(1))
    .min(1, "O campo de vacinas é obrigatório."),
  allergies: z.string().min(1, "O campo de alergias é obrigatório."),
  disability: z.object({
    types: z
      .array(z.string().min(1))
      .min(1, "O tipo de atendimento é obrigatório."),
    report: z.instanceof(File, { message: "O laudo é obrigatório." }),
  }),
  care: z.object({
    types: z
      .array(z.string().min(1))
      .min(1, "O tipo de atendimento é obrigatório."),
    referral: z.instanceof(File, {
      message: "O encaminhamento é obrigatório.",
    }),
  }),
  bpc: z.boolean(),
  householdIncome: z
    .string()
    .min(1, "A renda familiar é obrigatória.")
    .refine(
      (value) => Number(value.replace(/\D/g, "")) >= 20000,
      "A renda familiar deve ser pelo menos R$ 200,00.",
    ),
});

export const Kinship = z.object({
  rg: RG,
  cpf: CPF,
  alive: z.boolean(),
  name: z
    .string()
    .transform((v) => v.trim())
    .pipe(
      z
        .string()
        .min(2, "Nome muito curto")
        .refine((val) => val.split(/\s+/).length >= 2, {
          message: "Digite o nome completo do parente",
        }),
    ),
  occupation: z.string().min(2, "Profissão inválida"),
  type: z.string().min(1, "Informar o parentesco é obrigatório."),
});

export const Kinships = z.object({
  kinships: z.array(Kinship).min(1, "Cadastre pelo menos um parente."),
});

export const Guardian = z.object({
  name: z
    .string()
    .transform((v) => v.trim())
    .pipe(
      z
        .string()
        .min(2, "Nome muito curto")
        .max(100, "Nome muito longo")
        .regex(/^[a-zA-ZÀ-ÿ\s]+$/, "Nome deve conter apenas letras e espaços")
        .refine((val) => val.split(/\s+/).length >= 2, {
          message: "Digite o nome completo do responsável (nome e sobrenome)",
        }),
    ),
  contact: z.string().min(1, "Contato de emergência é obrigatório."),
  kinship: z.string().min(1, "Informar o parentesco é obrigatório."),
  address: Address,
});

export const Profile = z.object({
  role: z.enum(["student", "patient"], {
    message: "Tipo de eficiência é obrigatório.",
  }),
  photo: z.instanceof(File, { message: "A foto é obrigatória." }),
});

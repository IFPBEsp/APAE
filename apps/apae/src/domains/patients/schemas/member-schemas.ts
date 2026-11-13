import z, { ZodCoercedDate } from "zod";
import { parseCivilDate } from "@/lib/date";
import { photoFileSchema } from "@/lib/photo.schema";
import {
  cpfSchema,
  nomeCompletoSchema,
  rgSchema,
  telefoneSchema,
} from "@/lib/personal-data.schema";
import { addressSchema } from "@/schemas/shared/address.schema";

const OptionalRG = z
  .string()
  .refine(
    (value) => value === "" || rgSchema.safeParse(value).success,
    "Formato de RG inválido",
  );

const CNS = z
  .string()
  .min(1, "CNS é obrigatório")
  .min(18, "CNS deve ter pelo menos 18 dígitos")
  .regex(/^\d{3} \d{4} \d{4} \d{4}$/, "Formato de CNS inválido");

const NIS = z
  .string()
  .length(11, "O NIS deve ter exatamente 11 dígitos")
  .regex(/^\d+$/, "O NIS deve conter apenas números")
  .optional()
  .or(z.literal(""));

const civilDateSchema = (
  futureMessage: string,
  minYearsBack: number,
  minMessage: string,
) =>
  z.preprocess(
    (value) => parseCivilDate(value as string | Date | null | undefined),
    z
      .date({ error: "Data inválida" })
      .refine((date) => date <= new Date(), { message: futureMessage })
      .refine(
        (date) => {
          const minDate = new Date();
          minDate.setFullYear(minDate.getFullYear() - minYearsBack);
          return date >= minDate;
        },
        { message: minMessage },
      ),
  ) as unknown as ZodCoercedDate<Date>;

  const optionalCivilDateSchema = z
  .union([z.string(), z.date(), z.null(), z.undefined()])
  .transform((value): Date | null => {
    if (value === "" || value === null || value === undefined) {
      return null;
    }

    return parseCivilDate(value as string | Date) ?? null;
  })
  .refine(
    (date) => date === null || date <= new Date(),
    { message: "Data de emissão não pode ser futura" },
  )
  .refine(
    (date) => {
      if (date === null) return true;

      const minDate = new Date();
      minDate.setFullYear(minDate.getFullYear() - 50);
      return date >= minDate;
    },
    { message: "Data de emissão muito antiga" },
  );

export const Personal = z
  .object({
    name: nomeCompletoSchema,
    cpf: cpfSchema,
    phone: telefoneSchema,
    rg: z.object({
      number: OptionalRG,
      issuing: z.object({
        body: z
          .string()
          .max(10, "Órgão emissor muito longo")
          .refine((value) => value === "" || value.length >= 2, {
            message: "Órgão emissor inválido",
          })
          .refine(
            (value) => value === "" || /^[A-Z]{2,4}\/[A-Z]{2}$/.test(value),
            "Formato inválido. Use SSP/UF, PC/UF, etc.",
          ),
        date: optionalCivilDateSchema,
      }),
    }),
    cns: CNS,
    nis: NIS,
    birth: z.object({
      certificate: z
        .string()
        .min(1, "Certidão de nascimento é obrigatória")
        .min(40, "Número da certidão deve ter pelo menos 32 dígitos"),
      date: civilDateSchema(
        "Data de nascimento não pode ser futura",
        150,
        "Data de nascimento inválida",
      ).refine(
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
  .superRefine((data, ctx) => {
    if (!data.rg.number.trim()) return;

    if (data.rg.issuing.body === "") {
      ctx.addIssue({
        code: "custom",
        message: "Órgão emissor é obrigatório",
        path: ["rg", "issuing", "body"],
      });
    }

    if (!data.rg.issuing.date) {
      ctx.addIssue({
        code: "custom",
        message: "Data de emissão é obrigatória",
        path: ["rg", "issuing", "date"],
      });
      return;
    }

    if (data.rg.issuing.date <= data.birth.date) {
      ctx.addIssue({
        code: "custom",
        message: "A data de emissão do RG não pode ser anterior ao nascimento",
        path: ["rg", "issuing", "date"],
      });
    }
  });

export const Address = addressSchema;

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
    .refine((val) => {
      const num = parseFloat(val.replace(/[^\d]/g, ""));
      return !isNaN(num) && num >= 20000;
    }, "A renda familiar deve ser pelo menos R$ 200,00"),
});

export const Kinship = z.object({
  rg: OptionalRG,
  cpf: cpfSchema,
  alive: z.boolean(),
  name: nomeCompletoSchema,
  occupation: z.string().min(2, "Profissão inválida"),
  type: z.string().min(1, "Informar o parentesco é obrigatório."),
  isLegalGuardian: z.boolean().default(false).optional(),
});

export const Kinships = z.object({
  kinships: z.array(Kinship).min(1, "Cadastre pelo menos um parente."),
});

export const Guardian = z.object({
  name: nomeCompletoSchema,
  contact: telefoneSchema,

  kinship: z.string().min(1, "Informar o parentesco é obrigatório."),
  address: Address,
  residesWithPatient: z.boolean().optional(),
});

export const Profile = z.object({
  role: z.enum(["student", "patient"], {
    message: "Tipo de eficiência é obrigatório.",
  }),
  photo: photoFileSchema,
});

export type PersonalData = z.infer<typeof Personal>;
export type AddressData = z.infer<typeof Address>;
export type AdditionalsData = z.infer<typeof Additionals>;
export type KinshipData = z.infer<typeof Kinship>;
export type GuardianData = z.infer<typeof Guardian>;
export type ProfileData = z.infer<typeof Profile>;
export type KinshipsData = z.infer<typeof Kinships>;

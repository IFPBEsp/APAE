import { describe, expect, it } from "vitest";
import { Additionals, Personal, Guardian, Kinship, birthCertificateSchema } from "./member-schemas";
import { annualRegistryFormSchema } from "@/domains/patients/annual-registry/annual-registry.schema";
import { mapRegistryToFormValues } from "@/domains/patients/annual-registry/use-annual-registry-modal";

const MATRICULA = "12345678901234567890123456789012";
const MATRICULA_FORMATADA = "123456 78 90 1234 5 67890 123 4567890 12";
const MENSAGEM_OBRIGATORIA = "Informe o número da certidão de nascimento";

function firstError(value: string) {
  const result = birthCertificateSchema.safeParse(value);
  return result.success ? null : result.error.issues[0].message;
}

describe("birthCertificateSchema (#1122)", () => {
  it("aceita matrícula de 32 dígitos com máscara", () => {
    expect(birthCertificateSchema.safeParse(MATRICULA_FORMATADA).success).toBe(
      true,
    );
  });

  it("aceita matrícula de 32 dígitos sem máscara", () => {
    expect(birthCertificateSchema.safeParse(MATRICULA).success).toBe(true);
  });

  it("aceita certidão antiga com menos de 32 dígitos", () => {
    expect(birthCertificateSchema.safeParse("12345").success).toBe(true);
    expect(birthCertificateSchema.safeParse("1").success).toBe(true);
  });

  it("recusa campo vazio", () => {
    expect(firstError("")).toBe(MENSAGEM_OBRIGATORIA);
  });

  it("recusa texto sem nenhum dígito", () => {
    expect(firstError("   ")).toBe(MENSAGEM_OBRIGATORIA);
    expect(firstError("abc")).toBe(MENSAGEM_OBRIGATORIA);
  });

  it("recusa mais de 32 dígitos", () => {
    expect(birthCertificateSchema.safeParse(`${MATRICULA}1`).success).toBe(
      false,
    );
  });

  it("não cita 32 dígitos nas mensagens de erro", () => {
    expect(firstError("")).not.toMatch(/32/);
    expect(firstError(`${MATRICULA}1`)).not.toMatch(/32/);
  });
});

describe("Órgão emissor", () => {
  const validPersonalBase = {
    name: "João da Silva",
    cpf: "529.982.247-25",
    phone: "(83) 99999-9999",
    rg: {
      number: "1234567",
      issuing: {
        body: "SSP/PB",
        date: new Date(2020, 0, 1),
      },
    },
    cns: "123 4567 8901 2345",
    birth: {
      certificate: "123456 01 55 2010 1 12345 123 1234567 12",
      date: new Date(2010, 0, 1),
      place: "Campina Grande",
    },
  };

  const withIssuingBody = (body: string) =>
    Personal.safeParse({
      ...validPersonalBase,
      rg: {
        ...validPersonalBase.rg,
        issuing: { ...validPersonalBase.rg.issuing, body },
      },
    });

  it("aceita os dados pessoais sem a cópia opcional da certidão", () => {
    expect(Personal.safeParse(validPersonalBase).success).toBe(true);
  });

  it("aceita a cópia opcional da certidão como arquivo", () => {
    expect(
      Personal.safeParse({
        ...validPersonalBase,
        birth: {
          ...validPersonalBase.birth,
          file: new File(["cópia"], "certidao.pdf", {
            type: "application/pdf",
          }),
        },
      }).success,
    ).toBe(true);
  });

  it.each(["SSP/PB", "PC/RJ", "DETRAN/RJ", "MARINHA/RJ"])(
    "aceita órgão emissor válido: %s",
    (body) => {
      expect(withIssuingBody(body).success).toBe(true);
    },
  );

  it.each(["D/SP", "DETRAN/RJX", "DETRAN-RJ", "DETRAN/R", "DETRANRJ"])(
    "recusa órgão emissor inválido: %s",
    (body) => {
      expect(withIssuingBody(body).success).toBe(false);
    },
  );

  it("aceita órgão emissor com 13 caracteres", () => {
    expect(withIssuingBody("ABCDEFGHIJ/RJ").success).toBe(true);
  });

  it("recusa órgão emissor com mais de 13 caracteres", () => {
    expect(withIssuingBody("ABCDEFGHIJK/RJ").success).toBe(false);
  });
});

describe("Nomes com símbolos nos membros", () => {
  const nomes = [
    "Ana Clara D'Ávila",
    "Maria Sant’Ana",
    "Ana-Clara Souza",
    "José Silva Jr.",
  ];

  const validAddress = {
    cep: "58135-000",
    state: "PB",
    city: "Esperança",
    neighborhood: "Centro",
    street: "Rua A",
    number: "100",
    complement: "Casa",
  };

  it.each(nomes)("aceita nome no responsável: %s", (name) => {
    const result = Guardian.safeParse({
      name,
      contact: "(83) 99999-9999",
      kinship: "Mãe",
      address: validAddress,
    });

    expect(result.success).toBe(true);
  });

  it.each(nomes)("aceita nome no parente: %s", (name) => {
    const result = Kinship.safeParse({
      rg: "",
      cpf: "529.982.247-25",
      alive: true,
      name,
      occupation: "Professora",
      type: "Mãe",
    });

    expect(result.success).toBe(true);
  });
});

describe("Cartório, livro e folha (#1132)", () => {
  const CERTIDAO_ANTIGA = "12345";
  const base = {
    name: "João da Silva",
    cpf: "529.982.247-25",
    phone: "(83) 99999-9999",
    rg: { number: "", issuing: { body: "", date: null } },
    cns: "123 4567 8901 2345",
    birth: {
      certificate: CERTIDAO_ANTIGA,
      date: new Date(2010, 0, 1),
      place: "Campina Grande",
    },
  };

  const parse = (birth: Record<string, unknown>) =>
    Personal.safeParse({ ...base, birth: { ...base.birth, ...birth } });

  const errorPaths = (birth: Record<string, unknown>) => {
    const result = parse(birth);
    return result.success
      ? []
      : result.error.issues.map((i) => i.path.join("."));
  };

  it("exige os três campos com certidão de menos de 32 dígitos", () => {
    expect(errorPaths({})).toEqual(
      expect.arrayContaining([
        "birth.registryOffice",
        "birth.book",
        "birth.fls",
      ]),
    );
  });

  it("retorna erro apenas nos campos faltantes", () => {
    const paths = errorPaths({ registryOffice: "Cartório X", book: "  " });
    expect(paths).toContain("birth.book");
    expect(paths).toContain("birth.fls");
    expect(paths).not.toContain("birth.registryOffice");
  });

  it("aceita certidão curta com os três campos preenchidos", () => {
    expect(
      parse({ registryOffice: "Cartório X", book: "A", fls: "10" }).success,
    ).toBe(true);
  });

  it("exige os campos com 31 dígitos", () => {
    expect(errorPaths({ certificate: "1".repeat(31) })).toContain(
      "birth.fls",
    );
  });

  it("não valida os campos com 32 dígitos sem máscara", () => {
    expect(parse({ certificate: "1".repeat(32) }).success).toBe(true);
  });

  it("não valida os campos com 32 dígitos com máscara", () => {
    expect(
      parse({ certificate: "123456 78 90 1234 5 67890 123 4567890 12" })
        .success,
    ).toBe(true);
  });
});

describe("Campo Doenças opcional (#1151)", () => {
  const validAdditionalsBase = {
    medications: "Losartana",
    vaccines: ["BCG"],
    allergies: "Poeira",
    disability: {
      types: ["TEA"],
      report: new File([new Uint8Array(10)], "laudo.pdf", { type: "application/pdf" }),
    },
    care: {
      types: ["Psicologia"],
    },
    bpc: false,
    householdIncome: "R$ 1.500,00",
  };

  it("Additionals aceita doenças vazio, omitido ou preenchido", () => {
    expect(Additionals.safeParse({ ...validAdditionalsBase, diseases: "" }).success).toBe(true);
    expect(Additionals.safeParse({ ...validAdditionalsBase }).success).toBe(true);
    expect(
      Additionals.safeParse({ ...validAdditionalsBase, diseases: "Diabetes, Catapora" }).success,
    ).toBe(true);
  });

  const validAnnualRegistryBase = {
    year: "2026",
    bpc: "false" as const,
    familyIncome: "R$ 1.500,00",
    continuousMedication: "Losartana",
    allergies: "Poeira",
    vaccines: [{ name: "BCG" }],
    disorders: [{ name: "TEA" }],
    serviceTypes: [{ area: "Psicologia" }],
  };

  it("annualRegistryFormSchema aceita doenças vazio, omitido ou preenchido", () => {
    expect(
      annualRegistryFormSchema.safeParse({ ...validAnnualRegistryBase, diseases: "" }).success,
    ).toBe(true);
    expect(annualRegistryFormSchema.safeParse({ ...validAnnualRegistryBase }).success).toBe(true);
    expect(
      annualRegistryFormSchema.safeParse({
        ...validAnnualRegistryBase,
        diseases: "Hipertensão, Catapora",
      }).success,
    ).toBe(true);
  });

  it("mapRegistryToFormValues abre o campo vazio quando diseases é null, vazio ou 'Não informado'", () => {
    const baseRegistry = {
      bpc: false,
      familyIncome: 1500,
      continuousMedication: "Losartana",
    };

    expect(
      mapRegistryToFormValues("2026", { ...baseRegistry, diseases: null }, null).diseases,
    ).toBe("");
    expect(
      mapRegistryToFormValues("2026", { ...baseRegistry, diseases: "   " }, null).diseases,
    ).toBe("");
    expect(
      mapRegistryToFormValues("2026", { ...baseRegistry, diseases: "Não informado" }, null)
        .diseases,
    ).toBe("");
    expect(
      mapRegistryToFormValues("2026", { ...baseRegistry, diseases: "Diabetes" }, null).diseases,
    ).toBe("Diabetes");
  });
});

describe("Contato de emergência do responsável opcional (#1158)", () => {
  const validGuardianBase = {
    name: "Maria da Silva",
    kinship: "Mãe",
    address: {
      cep: "58135-000",
      state: "PB" as const,
      city: "Esperança",
      neighborhood: "Centro",
      street: "Rua A",
      number: "100",
      complement: "Casa",
    },
  };

  it("aceita contato de emergência vazio, só espaços ou omitido", () => {
    expect(Guardian.safeParse({ ...validGuardianBase, contact: "" }).success).toBe(true);
    expect(Guardian.safeParse({ ...validGuardianBase, contact: "   " }).success).toBe(true);
    expect(Guardian.safeParse({ ...validGuardianBase }).success).toBe(true);
  });

  it("aceita contato de emergência preenchido com formato válido", () => {
    expect(
      Guardian.safeParse({ ...validGuardianBase, contact: "(83) 99999-9999" }).success,
    ).toBe(true);
    expect(
      Guardian.safeParse({ ...validGuardianBase, contact: "(83) 3361-1234" }).success,
    ).toBe(true);
  });

  it("rejeita contato de emergência preenchido com formato inválido", () => {
    const result = Guardian.safeParse({ ...validGuardianBase, contact: "(83) 123" });
    expect(result.success).toBe(false);
    if (!result.success) {
      expect(result.error.issues[0].message).toBe("Número de telefone inválido");
    }
  });
});

describe("Campo Profissão do parente opcional (#1157)", () => {
  const validKinshipBase = {
    rg: "",
    cpf: "529.982.247-25",
    alive: true,
    name: "Maria da Silva",
    type: "Mãe",
  };

  it("aceita profissão vazia, omitida ou apenas espaços", () => {
    expect(Kinship.safeParse({ ...validKinshipBase, occupation: "" }).success).toBe(true);
    expect(Kinship.safeParse({ ...validKinshipBase }).success).toBe(true);
    expect(Kinship.safeParse({ ...validKinshipBase, occupation: "   " }).success).toBe(true);
  });

  it("recusa profissão com menos de 2 caracteres úteis quando preenchida", () => {
    const singleChar = Kinship.safeParse({ ...validKinshipBase, occupation: "A" });
    expect(singleChar.success).toBe(false);
    if (!singleChar.success) {
      expect(singleChar.error.issues[0].message).toBe("Profissão inválida");
    }

    const paddedSingleChar = Kinship.safeParse({ ...validKinshipBase, occupation: " A " });
    expect(paddedSingleChar.success).toBe(false);
  });

  it("aceita profissão com 2 ou mais caracteres", () => {
    expect(Kinship.safeParse({ ...validKinshipBase, occupation: "AB" }).success).toBe(true);
    expect(Kinship.safeParse({ ...validKinshipBase, occupation: "Agricultora" }).success).toBe(true);
  });
});

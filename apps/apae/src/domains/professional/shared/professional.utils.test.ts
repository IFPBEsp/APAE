import { describe, it, expect } from "vitest";
import { Professional, daysOfWeek, shifts } from "@/types/profissional";
import { ProfessionalFormValues } from "@/schemas/profissional.schema";
import {
  filterProfessionals,
  buildProfessionalPayload,
  mapProfessionalToForm,
  isValidFile,
  getStatusActionConfig,
} from "./professional.utils";

const makeProfessional = (overrides: Record<string, unknown> = {}) =>
  ({
    name: "Maria Silva",
    email: "maria@apae.org",
    cpf: "111.222.333-44",
    professionalDocument: "CRP-123",
    identityDocument: "1234567",
    phoneNumber: "83999999999",
    serviceArea: { area: "PSICOLOGIA" },
    address: {
      state: "PB",
      city: "Esperança",
      neighborhood: "Centro",
      street: "Rua A",
      number: "10",
      complement: "Casa",
      cep: "58135-000",
    },
    availabilities: [],
    ...overrides,
  }) as unknown as Professional;

const makeFormValues = (overrides: Record<string, unknown> = {}) =>
  ({
    fullName: "  Maria Silva  ",
    email: "  maria@apae.org ",
    cpf: " 111.222.333-44 ",
    rg: " 1234567 ",
    professionalDocument: " CRP-123 ",
    serviceArea: "PSICOLOGIA",
    phone: "83999999999",
    state: "PB",
    city: " Esperança ",
    neighborhood: " Centro ",
    street: " Rua A ",
    number: " 10 ",
    complement: " Casa ",
    cep: "58135-000",
    availability: [],
    ...overrides,
  }) as unknown as ProfessionalFormValues;

describe("filterProfessionals", () => {
  const maria = makeProfessional();
  const joao = makeProfessional({
    name: "João Souza",
    cpf: "999.888.777-66",
    professionalDocument: "CREFITO-55",
    serviceArea: { area: "FISIOTERAPIA" },
  });
  const list = [maria, joao];

  it("retorna todos quando o termo é vazio e a área é 'all'", () => {
    expect(filterProfessionals(list, "", "all")).toEqual(list);
  });

  it("filtra pelo nome, ignorando maiúsculas/minúsculas", () => {
    expect(filterProfessionals(list, "MARIA", "all")).toEqual([maria]);
  });

  it("filtra pelo documento profissional", () => {
    expect(filterProfessionals(list, "crefito", "all")).toEqual([joao]);
  });

  it("filtra pelo CPF", () => {
    expect(filterProfessionals(list, "999.888", "all")).toEqual([joao]);
  });

  it("filtra pela área de atuação", () => {
    expect(filterProfessionals(list, "", "FISIOTERAPIA")).toEqual([joao]);
  });

  it("combina busca e área (ambas precisam bater)", () => {
    expect(filterProfessionals(list, "maria", "FISIOTERAPIA")).toEqual([]);
  });

  it("não quebra com profissional sem name/cpf/professionalDocument", () => {
    const incompleto = makeProfessional({
      name: undefined,
      cpf: undefined,
      professionalDocument: undefined,
    });

    expect(() => filterProfessionals([incompleto], "x", "all")).not.toThrow();
    expect(filterProfessionals([incompleto], "x", "all")).toEqual([]);
  });

  it("retorna lista vazia quando a entrada é vazia", () => {
    expect(filterProfessionals([], "maria", "all")).toEqual([]);
  });
});

describe("buildProfessionalPayload", () => {
  it("monta o payload aplicando trim nos campos de texto", () => {
    const payload = buildProfessionalPayload(makeFormValues());

    expect(payload).toMatchObject({
      serviceArea: { area: "PSICOLOGIA" },
      phoneNumber: "83999999999",
      professionalDocument: "CRP-123",
      email: "maria@apae.org",
      cpf: "111.222.333-44",
      name: "Maria Silva",
      identityDocument: "1234567",
      address: {
        state: "PB",
        city: "Esperança",
        neighborhood: "Centro",
        street: "Rua A",
        number: "10",
        complement: "Casa",
        cep: "58135-000",
      },
    });
  });

  it("envia professionalDocument como null quando vazio ou só espaços", () => {
    expect(
      buildProfessionalPayload(makeFormValues({ professionalDocument: "   " }))
        .professionalDocument,
    ).toBeNull();
    expect(
      buildProfessionalPayload(
        makeFormValues({ professionalDocument: undefined }),
      ).professionalDocument,
    ).toBeNull();
  });

  it("usa string vazia em number e complement quando não informados", () => {
    const { address } = buildProfessionalPayload(
      makeFormValues({ number: undefined, complement: undefined }),
    );

    expect(address.number).toBe("");
    expect(address.complement).toBe("");
  });

  it("inclui apenas as disponibilidades marcadas, só com day e shift", () => {
    const payload = buildProfessionalPayload(
      makeFormValues({
        availability: [
          { day: daysOfWeek[0].id, shift: shifts[0].id, checked: true },
          { day: daysOfWeek[1].id, shift: shifts[0].id, checked: false },
        ],
      }),
    );

    expect(payload.availabilities).toEqual([
      { day: daysOfWeek[0].id, shift: shifts[0].id },
    ]);
  });

  it("devolve availabilities vazia quando availability é undefined", () => {
    const payload = buildProfessionalPayload(
      makeFormValues({ availability: undefined }),
    );

    expect(payload.availabilities).toEqual([]);
  });
});

describe("mapProfessionalToForm", () => {
  it("mapeia os campos do profissional para os valores do formulário", () => {
    const form = mapProfessionalToForm(makeProfessional());

    expect(form).toMatchObject({
      fullName: "Maria Silva",
      email: "maria@apae.org",
      cpf: "111.222.333-44",
      professionalDocument: "CRP-123",
      serviceArea: "PSICOLOGIA",
      phone: "83999999999",
      rg: "1234567",
      state: "PB",
      city: "Esperança",
      neighborhood: "Centro",
      street: "Rua A",
      number: "10",
      complement: "Casa",
      cep: "58135-000",
    });
  });

  it("usa string vazia para cpf, professionalDocument e complement ausentes", () => {
    const form = mapProfessionalToForm(
      makeProfessional({
        cpf: undefined,
        professionalDocument: undefined,
        address: {
          state: "PB",
          city: "Esperança",
          neighborhood: "Centro",
          street: "Rua A",
          number: "10",
          cep: "58135-000",
        },
      }),
    );

    expect(form.cpf).toBe("");
    expect(form.professionalDocument).toBe("");
    expect(form.complement).toBe("");
  });

  it("gera a matriz completa de disponibilidade, marcando as do profissional", () => {
    const form = mapProfessionalToForm(
      makeProfessional({
        availabilities: [
          {
            day: daysOfWeek[0].id.toUpperCase(),
            shift: shifts[0].id.toUpperCase(),
          },
        ],
      }),
    );

    expect(form.availability).toHaveLength(daysOfWeek.length * shifts.length);
    expect(form.availability.filter((a) => a.checked)).toEqual([
      { day: daysOfWeek[0].id, shift: shifts[0].id, checked: true },
    ]);
  });

  it("gera a matriz toda desmarcada quando availabilities é undefined", () => {
    const form = mapProfessionalToForm(
      makeProfessional({ availabilities: undefined }),
    );

    expect(form.availability).toHaveLength(daysOfWeek.length * shifts.length);
    expect(form.availability.every((a) => !a.checked)).toBe(true);
  });
});

describe("isValidFile", () => {
  const MB = 1024 * 1024;
  const makeFile = (type: string, size: number) =>
    new File([new Uint8Array(size)], "arquivo", { type });

  it.each([
    "application/pdf",
    "image/png",
    "image/jpeg",
    "image/jpg",
    "image/webp",
  ])("aceita o tipo %s", (type) => {
    expect(isValidFile(makeFile(type, 1024))).toBe(true);
  });

  it("rejeita tipo não permitido", () => {
    expect(isValidFile(makeFile("text/plain", 1024))).toBe(false);
  });

  it("rejeita arquivo vazio (size 0)", () => {
    expect(isValidFile(makeFile("application/pdf", 0))).toBe(false);
  });

  it("aceita arquivo com exatamente 5MB", () => {
    expect(isValidFile(makeFile("application/pdf", 5 * MB))).toBe(true);
  });

  it("rejeita arquivo maior que 5MB", () => {
    expect(isValidFile(makeFile("application/pdf", 5 * MB + 1))).toBe(false);
  });
});

describe("getStatusActionConfig", () => {
  it("retorna a configuração de 'Inativar' para o filtro 'activate'", () => {
    expect(getStatusActionConfig("activate")).toEqual({
      label: "Inativar",
      itemClass: "text-destructive focus:text-destructive",
      buttonClass: "",
    });
  });

  it("retorna a configuração de 'Reativar' para o filtro 'inactivate'", () => {
    expect(getStatusActionConfig("inactivate")).toEqual({
      label: "Reativar",
      itemClass: "text-green-600 focus:text-green-600",
      buttonClass: "bg-green-600 hover:bg-green-700 text-white",
    });
  });
});

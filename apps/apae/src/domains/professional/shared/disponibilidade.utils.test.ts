import { describe, it, expect } from "vitest";
import {
  daysOfWeek,
  shifts,
  AvailabilityType,
  AvailabilityDTO,
} from "@/types/profissional";
import {
  generateAvailabilityMatrix,
  buildAvailabilityMatrixFromDTOs,
} from "./disponibilidade.utils";

const expectedKeys = daysOfWeek.flatMap((d) =>
  shifts.map((s) => `${d.id}|${s.id}`),
);
const keysOf = (matrix: AvailabilityType[]) =>
  matrix.map((i) => `${i.day}|${i.shift}`);

const fullList = (checked: boolean): AvailabilityType[] =>
  daysOfWeek.flatMap((d) =>
    shifts.map((s) => ({ day: d.id, shift: s.id, checked })),
  );

describe("generateAvailabilityMatrix", () => {
  it("devolve todo dia x todo turno com checked: false quando a lista é vazia", () => {
    const result = generateAvailabilityMatrix([]);

    expect(result).toHaveLength(daysOfWeek.length * shifts.length);
    expect(keysOf(result)).toEqual(expectedKeys);
    expect(result.every((item) => item.checked === false)).toBe(true);
  });

  it("preserva os itens existentes quando a lista está completa", () => {
    const input = fullList(true);

    const result = generateAvailabilityMatrix(input);

    expect(result).toHaveLength(input.length);
    expect(result.every((item) => item.checked === true)).toBe(true);
  });

  it("preenche com checked: false o que falta e mantém o que veio marcado", () => {
    const input: AvailabilityType[] = [
      { day: daysOfWeek[0].id, shift: shifts[0].id, checked: true },
    ];

    const result = generateAvailabilityMatrix(input);

    expect(result).toHaveLength(daysOfWeek.length * shifts.length);
    expect(result.filter((i) => i.checked)).toHaveLength(1);
    expect(result[0]).toEqual(input[0]);
  });

  it("não quebra e ignora item de dia/turno que não existe, sem atrapalhar os itens válidos da mesma lista", () => {
    const validItem: AvailabilityType = {
      day: daysOfWeek[1].id,
      shift: shifts[0].id,
      checked: true,
    };
    const invalidItem: AvailabilityType = {
      day: "dia-inexistente",
      shift: "turno-inexistente",
      checked: true,
    };
    const input = [invalidItem, validItem];

    expect(() => generateAvailabilityMatrix(input)).not.toThrow();

    const result = generateAvailabilityMatrix(input);

    // a matriz continua completa e na ordem certa
    expect(result).toHaveLength(daysOfWeek.length * shifts.length);
    expect(keysOf(result)).toEqual(expectedKeys);

    // o inválido não aparece
    expect(result.some((i) => i.day === "dia-inexistente")).toBe(false);

    // o válido continua marcado, na posição esperada, e é o único checked
    const expectedIndex = expectedKeys.indexOf(
      `${validItem.day}|${validItem.shift}`,
    );
    expect(result[expectedIndex]).toEqual(validItem);
    expect(result.filter((i) => i.checked)).toHaveLength(1);
  });

  it("segue a ordem de daysOfWeek/shifts, não a da lista de entrada, mantendo cada checked na célula certa", () => {
    const targetDay = daysOfWeek[daysOfWeek.length - 1].id;
    const targetShift = shifts[0].id;

    // lista completa, só com um item marcado, e invertida
    const input = fullList(false)
      .map((item) =>
        item.day === targetDay && item.shift === targetShift
          ? { ...item, checked: true }
          : item,
      )
      .reverse();

    const result = generateAvailabilityMatrix(input);

    // a ordem segue daysOfWeek x shifts
    expect(keysOf(result)).toEqual(expectedKeys);

    // o item marcado aparece na posição esperada, e só ele
    const expectedIndex = expectedKeys.indexOf(`${targetDay}|${targetShift}`);
    expect(result[expectedIndex]).toEqual({
      day: targetDay,
      shift: targetShift,
      checked: true,
    });
    expect(result.filter((i) => i.checked)).toHaveLength(1);
  });
});

describe("buildAvailabilityMatrixFromDTOs", () => {
  it("devolve a matriz toda desmarcada quando não recebe DTOs", () => {
    const result = buildAvailabilityMatrixFromDTOs();

    expect(keysOf(result)).toEqual(expectedKeys);
    expect(result.every((item) => item.checked === false)).toBe(true);
  });

  it("marca como checked os DTOs recebidos, normalizando day/shift para minúsculo", () => {
    const dtos = [
      {
        day: daysOfWeek[1].id.toUpperCase(),
        shift: shifts[0].id.toUpperCase(),
      },
    ] as AvailabilityDTO[];

    const result = buildAvailabilityMatrixFromDTOs(dtos);
    const checked = result.filter((i) => i.checked);

    expect(checked).toEqual([
      { day: daysOfWeek[1].id, shift: shifts[0].id, checked: true },
    ]);
  });

  it("não quebra com DTO sem day/shift", () => {
    const dtos = [{}] as AvailabilityDTO[];

    expect(() => buildAvailabilityMatrixFromDTOs(dtos)).not.toThrow();
    expect(
      buildAvailabilityMatrixFromDTOs(dtos).every((i) => !i.checked),
    ).toBe(true);
  });
});

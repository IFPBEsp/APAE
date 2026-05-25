export function parseCivilDate(
  value: string | Date | null | undefined,
): Date | null {
  if (!value) return null;

  if (value instanceof Date) {
    return Number.isNaN(value.getTime())
      ? null
      : new Date(value.getFullYear(), value.getMonth(), value.getDate());
  }

  const isoMatch = /^([0-9]{4})-([0-9]{2})-([0-9]{2})/.exec(value);
  if (isoMatch) {
    const year = Number(isoMatch[1]);
    const month = Number(isoMatch[2]);
    const day = Number(isoMatch[3]);
    return new Date(year, month - 1, day);
  }

  const brMatch = /^([0-9]{2})\/([0-9]{2})\/([0-9]{4})$/.exec(value);
  if (!brMatch) return null;

  const day = Number(brMatch[1]);
  const month = Number(brMatch[2]);
  const year = Number(brMatch[3]);
  return new Date(year, month - 1, day);
}

export function formatCivilDateDisplayValue(
  value: string | Date | null | undefined,
): string {
  const date = parseCivilDate(value);
  if (!date) return "";

  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}

export function serializeCivilDate(
  value: string | Date | null | undefined,
): string | null {
  const date = parseCivilDate(value);
  if (!date) return null;

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

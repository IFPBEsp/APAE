import { clsx, type ClassValue } from "clsx";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export const separateAndTransformIntoNumber = (value: unknown, separator: string) => {
  if (typeof value == "string" && value.length) {
    return (value as string).split(separator).map((n) => parseInt(n));
  }
  return [NaN, NaN, NaN];
};

export const formatDatePTBR = (date: string) => {
  const dateUtc = new Date(date).setUTCHours(12);
  return format(dateUtc, "dd 'de' MMMM 'de' yyyy", { locale: ptBR });
};

import { clsx, type ClassValue } from "clsx"
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export const separaETransformaEmNumero = (valor: unknown, separador: string) => {
    if (typeof valor == "string" && valor.length) {
      return (valor as string).split(separador).map((n) => parseInt(n));
    }
    return [NaN, NaN, NaN];
};


export const formatDatePTBR = (date: string) => {
  const dateUtc = new Date(date).setUTCHours(12);
  return format(dateUtc, "dd 'de' MMMM 'de' yyyy" , { locale: ptBR })
}
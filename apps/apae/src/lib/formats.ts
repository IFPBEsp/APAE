function limit(value: string, max: number) {
  return value.length <= max ? value : value.slice(0, max);
}

export function formatCPF(value: string) {
  const numbers = limit(value.replace(/\D/g, ""), 11);
  return numbers
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

export function formatPhone(value: string) {
  const numbers = value.replace(/\D/g, "");

  if (numbers.length <= 10) {
    return numbers
      .replace(/(\d{2})(\d)/, "($1) $2")
      .replace(/(\d{4})(\d)/, "$1-$2");
  }

  return numbers
    .slice(0, 11)
    .replace(/(\d{2})(\d)/, "($1) $2")
    .replace(/(\d{5})(\d)/, "$1-$2");
}

export function formatRG(value: string) {
  const numbers = limit(value.replace(/\D/g, ""), 7);
  return numbers
    .replace(/(\d{1})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2");
}

export function formatIssuingBody(value: string) {
  return limit(value, 7)
    .toUpperCase()
    .replace(/[^A-Z\/]/g, "");
}

export function formatBirthCertificate(value: string) {
  const numbers = value.replace(/\D/g, "");
  const divisors = [6, 2, 2, 4, 1, 5, 3, 7];

  return divisors.reduce(
    (acc, curr, index) => {
      const regex = `${acc.regex}(\\d{${curr}})`;
      const groups = `${acc.groups} $${index + 2}`;
      const result = acc.result.replace(new RegExp(`${regex}(\\d)`), groups);

      return {
        regex: `${regex}\\ `,
        groups,
        result,
      };
    },
    {
      regex: "",
      groups: "$1",
      result: limit(numbers, 32),
    },
  ).result;
}

export function formatCEP(value: string) {
  const numbers = limit(value.replace(/\D/g, ""), 8);
  return numbers.replace(/(\d{5})(\d)/, "$1-$2");
}

export function formatCurrency(value: string) {
  const numbers = value.replace(/\D/g, "");
  return `R$ ${numbers
    .replace(/^0+/, "")
    .padStart(3, "0")
    .replace(/(\d)(?=(\d{3})+\d{2}$)/g, "$1.")
    .replace(/(\d{2})$/, ",$1")}`;
}

export function formatCNS(value: string) {
  const numbers = limit(value.replace(/\D/g, ""), 15);
  return numbers
    .replace(/(\d{3})(\d)/, "$1 $2")
    .replace(/(\d{4})(\d)/, "$1 $2")
    .replace(/(\d{4})(\d)/, "$1 $2");
}

export function capitalizeFirst(value: string): string {
  if (!value) return value;
  return value.charAt(0).toUpperCase() + value.slice(1);
}
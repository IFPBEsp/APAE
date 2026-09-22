import { generateAvailabilityMatrix } from "./disponibilidade.utils";

type StatusFilter = "activate" | "inactivate";

export function isValidFile(file: File): boolean {
  const allowedTypes = [
    "application/pdf",
    "image/png",
    "image/jpeg",
    "image/jpg",
    "image/webp",
  ];
  const maxSize = 5 * 1024 * 1024;
  return allowedTypes.includes(file.type) && file.size > 0 && file.size <= maxSize;
}

export function mapProfessionalToForm(professional: any) {
  const backendAvailabilities = professional.availabilities || [];
  const fullMatrix = generateAvailabilityMatrix(
    backendAvailabilities.map((a: any) => ({
      day: a.day.toLowerCase(),
      shift: a.shift.toLowerCase(),
      checked: true,
    })),
  );

  return {
    fullName: professional.name,
    email: professional.email,
    cpf: professional.cpf ?? "",
    professionalDocument: professional.professionalDocument ?? "",
    serviceArea: professional.serviceArea.area,
    phone: professional.phoneNumber,
    rg: professional.identityDocument,
    state: professional.address.state,
    city: professional.address.city,
    neighborhood: professional.address.neighborhood,
    street: professional.address.street,
    number: professional.address.number,
    complement: professional.address.complement ?? "",
    cep: professional.address.cep,
    availability: fullMatrix,
  };
}

export function buildUpdatePayload(values: any, availabilities: any[]) {
  return {
    serviceArea: { area: values.serviceArea },
    phoneNumber: values.phone,
    professionalDocument: values.professionalDocument?.trim() || null,
    email: values.email.trim(),
    cpf: values.cpf.trim(),
    name: values.fullName.trim(),
    identityDocument: values.rg.trim(),
    address: {
      state: values.state,
      city: values.city.trim(),
      neighborhood: values.neighborhood.trim(),
      street: values.street.trim(),
      number: values.number?.trim(),
      complement: values.complement?.trim() ?? "",
      cep: values.cep,
    },
    availabilities,
  };
}

export function buildRegisterPayload(values: any) {
  const availabilities = values.availability
    .filter((d: any) => d?.checked)
    .map((d: any) => ({ day: d?.day, shift: d?.shift }));

  return {
    serviceArea: { area: values.serviceArea },
    phoneNumber: values.phone,
    professionalDocument: values.professionalDocument?.trim() || null,
    email: values.email.trim(),
    cpf: values.cpf.trim(),
    name: values.fullName.trim(),
    identityDocument: values.rg.trim(),
    address: {
      state: values.state,
      city: values.city.trim(),
      neighborhood: values.neighborhood.trim(),
      street: values.street.trim(),
      number: values.number?.trim(),
      complement: values.complement?.trim() ?? "",
      cep: values.cep,
    },
    availabilities,
  };
}

export function getStatusActionConfig(statusFilter: StatusFilter) {
  return {
    label: statusFilter === "activate" ? "Inativar" : "Reativar",
    itemClass: statusFilter === "activate"
      ? "text-destructive focus:text-destructive"
      : "text-green-600 focus:text-green-600",
    buttonClass: statusFilter === "activate"
      ? ""
      : "bg-green-600 hover:bg-green-700 text-white",
  };
}

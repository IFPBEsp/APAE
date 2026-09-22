import { ProfessionalStatusFilter, type Professional } from "@/types/profissional";
import { extractCheckedAvailabilities, toAvailabilityMatrix } from "./disponibilidade.utils";

type ProfessionalListItem = Pick<Professional, "id" | "name" | "cpf" | "professionalDocument" | "serviceArea" | "phoneNumber"> & {
  serviceArea?: { area?: string | null };
};

type AvailabilitySelection = {
  day?: string;
  shift?: string;
  checked?: boolean;
};

type ProfessionalFormValues = {
  serviceArea: string;
  phone: string;
  professionalDocument?: string | null;
  email: string;
  cpf: string;
  fullName: string;
  rg: string;
  state: string;
  city: string;
  neighborhood: string;
  street: string;
  number?: string;
  complement?: string | null;
  cep: string;
  availability?: AvailabilitySelection[];
};

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

export function filterProfessionals(
  professionals: ProfessionalListItem[],
  filters: { searchTerm?: string; areaFilter?: string } = {},
) {
  const searchTerm = (filters.searchTerm ?? "").trim().toLowerCase();
  const areaFilter = filters.areaFilter ?? "all";

  return professionals.filter((prof) => {
    const name = prof.name?.toLowerCase() ?? "";
    const document = prof.professionalDocument?.toLowerCase() ?? "";
    const cpf = prof.cpf?.toLowerCase() ?? "";
    const matchesSearch =
      !searchTerm ||
      name.includes(searchTerm) ||
      document.includes(searchTerm) ||
      cpf.includes(searchTerm);
    const matchesArea =
      areaFilter === "all" || prof.serviceArea?.area === areaFilter;

    return matchesSearch && matchesArea;
  });
}

export function getProfessionalAreaOptions(
  professionals: ProfessionalListItem[] = [],
) {
  return [
    "all",
    ...Array.from(
      new Set(
        professionals
          .map((professional) => professional.serviceArea?.area)
          .filter((area): area is string => Boolean(area)),
      ),
    ),
  ];
}

export function mapProfessionalToForm(professional: Partial<Professional> & {
  serviceArea?: { area?: string | null };
  address?: Partial<Professional["address"]>;
}) {
  const backendAvailabilities = professional.availabilities ?? [];
  const fullMatrix = toAvailabilityMatrix(backendAvailabilities);

  return {
    fullName: professional.name ?? "",
    email: professional.email ?? "",
    cpf: professional.cpf ?? "",
    professionalDocument: professional.professionalDocument ?? "",
    serviceArea: professional.serviceArea?.area ?? "",
    phone: professional.phoneNumber ?? "",
    rg: professional.identityDocument ?? "",
    state: professional.address?.state ?? "",
    city: professional.address?.city ?? "",
    neighborhood: professional.address?.neighborhood ?? "",
    street: professional.address?.street ?? "",
    number: professional.address?.number ?? "",
    complement: professional.address?.complement ?? "",
    cep: professional.address?.cep ?? "",
    availability: fullMatrix,
  };
}

export function buildProfessionalPayload(
  values: ProfessionalFormValues,
  availabilities = extractCheckedAvailabilities(values.availability ?? []),
) {
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
      number: values.number?.trim() ?? "",
      complement: values.complement?.trim() ?? "",
      cep: values.cep,
    },
    availabilities,
  };
}

export function buildUpdatePayload(
  values: ProfessionalFormValues,
  availabilities = extractCheckedAvailabilities(values.availability ?? []),
) {
  return buildProfessionalPayload(values, availabilities);
}

export function buildRegisterPayload(values: ProfessionalFormValues) {
  return buildProfessionalPayload(values, extractCheckedAvailabilities(values.availability ?? []));
}

export function getStatusActionConfig(statusFilter: ProfessionalStatusFilter) {
  return {
    label: statusFilter === "activate" ? "Inativar" : "Reativar",
    itemClass:
      statusFilter === "activate"
        ? "text-destructive focus:text-destructive"
        : "text-green-600 focus:text-green-600",
    buttonClass:
      statusFilter === "activate"
        ? ""
        : "bg-green-600 hover:bg-green-700 text-white",
  };
}

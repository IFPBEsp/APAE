import { Professional } from "@/types/profissional";

export function filterProfessionals(
  professionals: Professional[],
  searchTerm: string,
  areaFilter: string
): Professional[] {
  return professionals.filter((prof) => {
    const name = prof.name?.toLowerCase() || "";
    const document = prof.professionalDocument?.toLowerCase() || "";
    const term = searchTerm.toLowerCase();

    const matchesSearch = name.includes(term) || document.includes(term);

    const matchesArea =
      areaFilter === "all" || prof.serviceArea.area === areaFilter;

    return matchesSearch && matchesArea;
  });
}

import { ProfessionalFormValues } from "@/schemas/profissional.schema";

export function buildProfessionalPayload(values: ProfessionalFormValues) {
  const availabilities = (values.disponibilidade || [])
    .filter((d) => d?.checked)
    .map((d) => ({
      day: d?.dia,
      shift: d?.turno,
    }));

  return {
    serviceArea: { area: values.areaAtendimento },
    phoneNumber: values.telefone,
    professionalDocument: values.documentoProfissional?.trim() || null,
    email: values.email.trim(),
    name: values.nomeCompleto.trim(),
    identityDocument: values.rg.trim(),
    address: {
      state: values.estado,
      city: values.cidade.trim(),
      neighborhood: values.bairro.trim(),
      street: values.rua.trim(),
      number: values.numero?.trim() ?? "",
      complement: values.complemento?.trim() ?? "",
      cep: values.cep,
    },
    availabilities,
  };
}

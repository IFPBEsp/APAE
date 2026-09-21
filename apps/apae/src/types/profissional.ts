import { ServiceArea } from "./service-area";

export const diasDaSemana = [
  { id: "segunda", label: "Segunda" },
  { id: "terca", label: "Terça" },
  { id: "quarta", label: "Quarta" },
  { id: "quinta", label: "Quinta" },
  { id: "sexta", label: "Sexta" },
];

export const turnos = [
  { id: "manha", label: "Manhã" },
  { id: "tarde", label: "Tarde" },
];

export type AvailabilityMatrix = {
  dia: string;
  turno: string;
  checked: boolean;
};

export interface AvailabilityDTO {
  day: string;
  shift: string;
}

export interface Professional {
  id: string;
  userId?: string;
  serviceArea: ServiceArea;
  phoneNumber: string;
  professionalDocument: string | null;
  email: string;
  cpf: string;
  name: string;
  identityDocument: string;
  address: Address;
  availabilities: AvailabilityDTO[];
  ativo: boolean;
  active?: boolean;
  profilePhoto?: string | null;
  profilePhotoUrl?: string | null;
}

export interface Address {
  cep: string;
  city: string;
  state: string;
  neighborhood: string;
  street: string;
  number?: string;
  complement?: string;
}
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

export type AvailabilityType = {
  dia: string;
  turno: string;
  checked: boolean;
};

export interface Availability {
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
  name: string;
  identityDocument: string;
  healthSector?: string | null;
  availabilities: Availability[];
  ativo: boolean;
  profilePhoto?: string | null;
}

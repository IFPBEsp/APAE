import { ServiceArea } from "./service-area";

export const daysOfWeek = [
  { id: "segunda", label: "Segunda" },
  { id: "terca", label: "Terça" },
  { id: "quarta", label: "Quarta" },
  { id: "quinta", label: "Quinta" },
  { id: "sexta", label: "Sexta" },
];

export const shifts = [
  { id: "manha", label: "Manhã" },
  { id: "tarde", label: "Tarde" },
];

export type AvailabilityType = {
  day: string;
  shift: string;
  checked: boolean;
};

export interface Availability {
  day: string;
  shift: string;
}

export interface Professional {
  id: string;
  serviceArea: ServiceArea;
  phoneNumber: string;
  professionalDocument: string;
  email: string;
  name: string;
  identityDocument: string;
  address: Address;
  availabilities: Availability[];
  active: boolean;
  profilePhotoUrl?: string;
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

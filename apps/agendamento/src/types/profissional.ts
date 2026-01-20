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

export type DisponibilidadeType = {
  dia: string;
  turno: string;
  checked: boolean;
};

export interface Disponibilidade {
  day: string;
  shift: string;
}

export interface Endereco {
  cep: string;
  city: string;
  state: string;
  neighborhood: string;
  street: string;
  number?: string;
  complement?: string;
}

export interface Profissional {
  id: string;
  serviceArea: ServiceArea;
  phoneNumber: string;
  professionalDocument: string;
  email: string;
  name: string;
  identityDocument: string;
  address: Endereco;
  availabilities: Disponibilidade[];
}

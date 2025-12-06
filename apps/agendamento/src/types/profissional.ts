import { ServiceArea } from "./service-area";

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
  availabity: Disponibilidade;
}

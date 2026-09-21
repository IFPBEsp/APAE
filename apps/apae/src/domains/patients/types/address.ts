export interface Address {
  cep: string;
  state: string;
  city: string;
  district: string;
  street: string;
}

export interface AddressResponse {
  city: string;
  street: string;
  neighborhood: string;
  state: string;
  cep: string;
  number: string;
  complement?: string;
}

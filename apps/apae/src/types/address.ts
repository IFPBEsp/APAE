export interface AddressData {
  cep: string;
  state: string;
  city: string;
  district: string;
  street: string;
}

export interface AddressResponse {
  street?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
}

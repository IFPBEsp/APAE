export interface Kinship {
  type: string;
  rg: string;
  cpf: string;
  alive: boolean;
  name: string;
  occupation: string;
}

export interface KinshipData {
  kinships: Kinship[];
}

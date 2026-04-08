export interface KinshipData {
  type: string;
  rg: string;
  cpf: string;
  alive: boolean;
  name: string;
  occupation: string;
}

export interface KinshipInputProps {
  index: number;
  onRemove: (index: number) => void;
}

export interface Disorder {
  id: string;
  name: string;
  hasPatient?: boolean;
}

export interface DisorderOption {
  label: string;
  value: string;
}

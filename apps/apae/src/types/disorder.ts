export interface Disorder {
  id: string;
  name: string;
  hasPatient: boolean;
}

export interface CreateDisorderDTO {
  name: string;
}

export interface UpdateDisorderDTO {
  name: string;
}

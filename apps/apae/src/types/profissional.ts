import { ServiceArea } from "./service-area";

export const DAYS = ["segunda", "terca", "quarta", "quinta", "sexta"] as const;
export const SHIFTS = ["manha", "tarde"] as const;

export type Day = (typeof DAYS)[number];
export type Shift = (typeof SHIFTS)[number];

export const DAY_LABEL: Record<Day, string> = {
  segunda: "Segunda",
  terca: "Terça",
  quarta: "Quarta",
  quinta: "Quinta",
  sexta: "Sexta",
};

export const SHIFT_LABEL: Record<Shift, string> = {
  manha: "Manhã",
  tarde: "Tarde",
};

export const daysOfWeek = DAYS.map((day) => ({
  id: day,
  label: DAY_LABEL[day],
}));

export const shifts = SHIFTS.map((shift) => ({
  id: shift,
  label: SHIFT_LABEL[shift],
}));

export type AvailabilityType = {
  day: Day | string;
  shift: Shift | string;
  checked: boolean;
};

export interface Availability {
  day: string;
  shift: string;
  checked?: boolean;
}

export type ProfessionalStatusFilter = "activate" | "inactivate";

export interface Professional {
  id: string;
  userId?: string;
  serviceArea: ServiceArea;
  phoneNumber: string;
  professionalDocument: string | null;
  email: string;
  cpf: string;
  name: string;
  identityDocument: string;
  address: Address;
  availabilities: Availability[];
  active?: boolean;
  ativo?: boolean;
  profilePhoto?: string | null;
  profilePhotoUrl?: string | null;
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

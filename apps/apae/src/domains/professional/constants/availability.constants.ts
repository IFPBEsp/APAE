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

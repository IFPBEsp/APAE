export const HEALTH_AREAS = [
  "Medicina",
  "Enfermagem",
  "Fisioterapia",
  "Psicologia",
  "Nutrição",
] as const;

export type HealthArea = (typeof HEALTH_AREAS)[number];

export const HEALTH_AREAS_OPTIONS = HEALTH_AREAS.map((h) => ({ value: h, label: h }));

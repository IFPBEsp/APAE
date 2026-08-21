export type Vaccine = Readonly<{
  id: string;
  name: string;
  hasPatient: boolean;
}>;

export type CreateVaccineParams = Readonly<{
  name: string;
}>;

export type UpdateVaccineParams = Readonly<{
  id: string;
  name: string;
}>;

export type DeleteVaccineParams = Readonly<{
  id: string;
}>;
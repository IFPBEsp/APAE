export type Vaccine = Readonly<{
  id: string;
  name: string;
  hasPatient: boolean;
}>;
<<<<<<< HEAD
=======

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
>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas)

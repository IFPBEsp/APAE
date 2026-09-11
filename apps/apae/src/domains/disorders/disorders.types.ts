export type Disorder = Readonly<{
  id: string;
  name: string;
  hasPatient: boolean;
}>;

export type CreateDisorderParams = Readonly<{
  name: string;
}>;

export type UpdateDisorderParams = Readonly<{
  id: string;
  name: string;
}>;

export type DeleteDisorderParams = Readonly<{
  id: string;
}>;

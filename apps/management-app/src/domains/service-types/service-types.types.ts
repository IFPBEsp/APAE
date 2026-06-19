export interface ServiceType {
  id: string | number;
  name: string;
}

export interface CreateServiceTypeDTO {
  name: string;
}

export type UpdateServiceTypeDTO = CreateServiceTypeDTO;

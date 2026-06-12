export interface ServiceType {
  id: string | number;
  area: string;
}

export interface CreateServiceTypeDTO {
  area: string;
}

export type UpdateServiceTypeDTO = CreateServiceTypeDTO;

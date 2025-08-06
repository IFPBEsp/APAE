export type PatientStatus = "Ativo" | "Inativo" | "Em Fila";

export interface Patient {
  id: string;
  name: string;
  cpf: string;
  contact: string;
  city: string;
  status: PatientStatus;
  avatarUrl: string;
}

export const patients: Patient[] = [
  {
    id: "1",
    name: "Fulano",
    cpf: "999.999.999-00",
    contact: "(99) 99999-9999",
    city: "Esperança",
    status: "Ativo",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026704d",
  },
  {
    id: "2",
    name: "Ciclano",
    cpf: "888.888.888-11",
    contact: "(88) 88888-8888",
    city: "Esperança",
    status: "Inativo",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026705d",
  },
  {
    id: "3",
    name: "Beltrano",
    cpf: "777.777.777-22",
    contact: "(77) 77777-7777",
    city: "Esperança",
    status: "Em Fila",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026706d",
  },
  {
    id: "4",
    name: "Teste",
    cpf: "666.666.666-33",
    contact: "(66) 66666-6666",
    city: "Esperança",
    status: "Ativo",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026707d",
  },
  {
    id: "5",
    name: "Outro teste",
    cpf: "666.666.666-33",
    contact: "(66) 66666-6666",
    city: "Esperança",
    status: "Ativo",
    avatarUrl: "https://i.pravatar.cc/150?u=a042581f4e29026707d",
  },
];

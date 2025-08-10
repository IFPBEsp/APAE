export type FileItem = {
  id: number;
  name: string;
  type: string;
  url: string;
  year: string;
};

export const mockFiles: FileItem[] = [
  { id: 1, name: "laudo.pdf", type: "laudo", url: "", year: "2025" },
  { id: 2, name: "encaminhamento.pdf", type: "encaminhamento", url: "", year: "2024" },
  { id: 3, name: "historico-escolar.pdf", type: "escolar", url: "", year: "2023" },
];

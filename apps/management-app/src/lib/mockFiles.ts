export type FileItem = {
  id: string;
  name: string;
  category: "pessoal" | "medico" | "escolar";
  type: string;
  url: string;
  year: string;
};

export const mockFiles: FileItem[] = [
  { id: "1", name: "laudo.pdf", type: "laudo", url: "", year: "2025", category: "medico" },
  { id: "2", name: "encaminhamento.pdf", type: "encaminhamento", url: "", year: "2024", category: "medico" },
  { id: "3", name: "historico-escolar.pdf", type: "historico", url: "", year: "2023", category: "escolar" },
  { id: "4", name: "rg.pdf", type: "rg", url: "", year: "2023", category: "pessoal" },
  { id: "5", name: "cpf.pdf", type: "cpf", url: "", year: "2024", category: "pessoal" },
];

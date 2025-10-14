export interface Endereco {
  estado: string;
  cidade: string;
  bairro: string;
  rua: string;
  numero?: string;
  complemento?: string;
  cep: string;
}

export interface Profissional {
  id: string;
  nome: string;
  email: string;
  docProfissional: string;
  areaDaSaude: string;
  telefone: string;
  cpf: string;
  rg: string;
  endereco: Endereco;
  ativo: boolean;
}

export interface Patient {
  id: string; 
  nome: string;
  cpf: string;
  status: 'Ativo' | 'Inativo' | 'Em Fila';
  urlFoto: string;
  contato: {
    telefone: string;
  };
  cidade: string; 
}

export interface Page<T> {
  content: T[];
}
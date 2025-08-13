// types.ts
export interface Patient {
  id: string;
  nome: string; // Tornar opcional
  cpf: string;
  status: 'Ativo' | 'Inativo' | 'Em Fila';
  urlFoto?: string;
  contato: {
    telefone: string;
  };
  cidade?: string;
}
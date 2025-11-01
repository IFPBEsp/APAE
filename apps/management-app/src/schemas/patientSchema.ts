export interface PatientCardData {
  id: string; 
  urlFoto: string | null; 
  nome: string;    
  cpf: string;
  cidade: string;
  contato: {
    telefone: string;
  };
}
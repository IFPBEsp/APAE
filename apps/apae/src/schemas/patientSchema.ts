export interface PatientCardData {
  id: string;
  urlFoto: string | null;
  fullName: string | null;
  cpf: string | null;      
  contact: string | null;  
  address: {               
    city: string | null;
  } | null;               
}
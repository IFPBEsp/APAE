export interface PatientCardData {
  isDeleted?: boolean;
  isStudent?: boolean;
  id: string;
  photoUrl: string | null;
  fullName: string | null;
  cpf: string | null;
  contact: string | null;
  address: {
    city: string | null;
  } | null;
}

export type DocumentCategory = "medical" | "personal" | "school";

export interface PatientDocument {
  id: string;
  name: string;
  category: string;
  type: string;
  url: string;
  year: string | number;
}

export interface ListPatientDocumentsParams {
  category: DocumentCategory;
  type?: string;
  year?: string | number;
}

export interface UploadPatientDocumentParams {
  category: string;
  file: File;
  type: string;
  year?: string | number;
}

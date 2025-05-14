package br.org.apae.documentos_medicos.application;

import org.springframework.web.multipart.MultipartFile;
import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public interface MedicalDocumentService {

    void saveFile(MedicalDocumentUploadDTO dtoObject, MultipartFile multipartFile);

    MedicalDocumentResponseDTO listMedicalDocument(String patientId, Integer year);

    MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type);

    MedicalDocumentResponseDTO getDocumentHistoryByType(String patientId, MedcialDocumentType type);

    byte[] viewPatientMedicalDocuments(String patientId, String path);

    void deleteDocument(String patientId, String fileName);
}

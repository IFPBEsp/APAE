package br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado;

import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos.DocumentObjectRequestDTO;
import br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos.DocumentsResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface IScannedDocumentManager {
    void createBucket(UUID patientId, String authorizationHeader);
    void removeBucket(UUID patientId);
    void saveFile(DocumentObjectRequestDTO dto, MultipartFile file,  String authorizationHeader);
    DocumentsResponseDTO listDocument(UUID patientId, String category, Integer year);
    DocumentsResponseDTO listDocumentByType(UUID patientId, Integer year, String category, String type);
    DocumentsResponseDTO getDocumentHistoryByType(UUID patientId, String category, String type);
    byte[] viewPatientMedicalDocuments(UUID patientId, String path);
    void deleteDocument(UUID patientId, String fileName);
}

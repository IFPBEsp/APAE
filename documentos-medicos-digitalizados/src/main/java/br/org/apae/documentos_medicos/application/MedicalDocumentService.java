package br.org.apae.documentos_medicos.application;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;
import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public interface MedicalDocumentService {

    void saveFile(MedicalDocumentUploadDTO dtoObject, MultipartFile multipartFile);

    MedicalDocumentResponseDTO listMedicalDocument(String patientId, Integer year);

    MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type);

    MedicalDocumentResponseDTO historicoTipoDocumento(String patientId, MedcialDocumentType type);

    MedicalDocumentResponseDTO visualizarDocumentosMedicosPaciente(UUID pacienteId, UUID documentoId);

    void desativarDocumento(UUID pacienteId, UUID documentoId);
}

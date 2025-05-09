package br.org.apae.documentos_medicos.application;

import java.util.List;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public interface MedicalDocumentService {

    void saveFile(MedicalDocumentUploadDTO dtoObject);

    List<MedicalDocumentResponseDTO> listMedicalDocument(String patientId, Integer year);

    List<MedicalDocumentResponseDTO> listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type);

}

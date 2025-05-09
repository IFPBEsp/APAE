package br.org.apae.documentos_medicos.application;

import java.util.List;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;

public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    @Override
    public void saveFile(MedicalDocumentUploadDTO dtoObject) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveFile'");
    }

    @Override
    public List<MedicalDocumentResponseDTO> listMedicalDocument(String patientId, Integer year) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocument'");
    }

    @Override
    public List<MedicalDocumentResponseDTO> listMedicalDocumentByType(String patientId, Integer year,
            MedcialDocumentType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocumentByType'");
    }

}
package br.org.apae.documentos_medicos.infrastructure.persistence.storage;

import java.util.List;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.repositories.MinioStorage;

public class MinioStorageImpl implements MinioStorage {

    @Override
    public void uploadFile(MedicalDocumentUploadDTO dtoObject) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
    }

    @Override
    public List<String> listMedicalDocumentUrls(String patientId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocumentUrls'");
    }

    @Override
    public List<String> listMedicalDocumentUrlsByType(String patientId, MedcialDocumentType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocumentUrlsByType'");
    }

    @Override
    public byte[] getMedicalDocument(String patientId, String fileName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMedicalDocument'");
    }

}

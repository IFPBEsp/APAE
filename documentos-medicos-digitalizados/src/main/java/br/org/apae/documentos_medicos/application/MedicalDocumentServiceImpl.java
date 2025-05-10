package br.org.apae.documentos_medicos.application;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@Service
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private MinioStorage minioMedicalDocumentStorage;
    private StorageClient storageClient;
    private static final String FOLDER_NAME = "documentos-medicos";

    @Autowired
    public MedicalDocumentServiceImpl(MinioStorage minioMedicalDocumentStorage, StorageClient storageClient) {
        this.minioMedicalDocumentStorage = minioMedicalDocumentStorage;
        this.storageClient = storageClient;
    }

    @Override
    public void saveFile(MedicalDocumentUploadDTO dtoObject, MultipartFile multipartFile) {
        // validateBucket(dtoObject.patientId());

        try {
            String bucket = dtoObject.getPatientId();
            String path = FOLDER_NAME + "/" + 
                            dtoObject.getYear() + "/" +
                            MedcialDocumentType.valueOf(dtoObject.getDocumentType()).getPrefix() + "/" + 
                            multipartFile.getOriginalFilename();
                            
            byte[] file = multipartFile.getBytes();

            minioMedicalDocumentStorage.uploadFile(bucket, path, file);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage(), e);
        }
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocument(String patientId, Integer year) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocument'");
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year,
            MedcialDocumentType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocumentByType'");
    }

    private void validateBucket(String bucket) {
        if (!storageClient.bucketExists(bucket)) {
            throw new RuntimeException("Bucket não existe: " + bucket);
        }
    }
}
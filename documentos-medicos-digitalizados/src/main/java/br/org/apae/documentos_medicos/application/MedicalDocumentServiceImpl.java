package br.org.apae.documentos_medicos.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentRequestDTO;
import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.models.MedicalDocument;
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
        return null;
}



    private void validateBucket(String bucket) {
        if (!storageClient.bucketExists(bucket)) {
            throw new RuntimeException("Bucket não existe: " + bucket);
        }
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year,
            MedcialDocumentType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listMedicalDocumentByType'");
    }

    @Override
    public MedicalDocumentResponseDTO historicoTipoDocumento(String patientId, MedcialDocumentType type) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'historicoTipoDocumento'");
    }

    @Override
    public MedicalDocumentResponseDTO visualizarDocumentosMedicosPaciente(UUID pacienteId, UUID documentoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visualizarDocumentosMedicosPaciente'");
    }

    @Override
    public MedicalDocumentResponseDTO atualizarDocumento(UUID pacienteId, UUID documentoId,
            MedicalDocumentRequestDTO documentoAtualizado) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizarDocumento'");
    }

    @Override
    public void desativarDocumento(UUID pacienteId, UUID documentoId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'desativarDocumento'");
    }
}
package br.org.apae.documentos_medicos.application;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

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
        try {
            String prefix = FOLDER_NAME;
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId + " no ano " + year);
            }

            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(
                patientId, objectNames, 
                2);

            return new MedicalDocumentResponseDTO(
                patientId, 
                presignedUrls
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar documentos: " + e.getMessage(), e);
        }
    }

    
    @Override
    public MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type) {
        try {
            String prefix = FOLDER_NAME + "/" + year + "/"+ type.getPrefix() + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId + " do' tipo " + type);
            }

            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(
                patientId, objectNames, 
                2);

            return new MedicalDocumentResponseDTO(
                patientId, 
                presignedUrls
            );

            } catch (Exception e) {
                throw new RuntimeException("Erro ao listar documentos do tipo: " + e.getMessage(), e);
            }
        }
        
        
    @Override
    public MedicalDocumentResponseDTO historyDocumentByType(String patientId, MedcialDocumentType type) {
        try {
            String prefix = FOLDER_NAME + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);
                
            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum histórico encontrado para o paciente " + patientId + " do tipo " + type);
            }

            Stream<String> stream = objectNames.stream().filter(ObjectName -> objectNames.contains(type.getPrefix()));

            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(patientId, stream.toList(), 2);

    
            return new MedicalDocumentResponseDTO(
                patientId, 
                presignedUrls
            );
    
            } catch (Exception e) {
                 throw new RuntimeException("Erro ao buscar histórico de documentos: " + e.getMessage(), e);
            }
    }
    
    
    @Override
    public MedicalDocumentResponseDTO viewPatientDocument(String patientId, String documentId) {
        try {
            String prefix = FOLDER_NAME + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);
            
            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId + " com esse nome: " + documentId);
            }
            
            Stream<String> stream = objectNames.stream().filter(ObjectName -> ObjectName.endsWith("/" + documentId));

            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(patientId, stream.toList(), 2);

    
            return new MedicalDocumentResponseDTO(
                patientId, 
                presignedUrls
            );

            } catch (Exception e) {
            throw new RuntimeException("Erro ao visualizar o documento: " + e.getMessage(), e);
        }
    }
    @Override
    public void deleteDocument(String patientId, String documentId) {
        try {
            String prefix = FOLDER_NAME + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);
            
            if (objectNames.isEmpty()) {
                throw new RuntimeException("Nenhum documento encontrado para o paciente " + patientId);
            }
            
            List<String> stream = objectNames.stream().filter(ObjectName -> ObjectName.endsWith("/" + documentId)).toList();

            if (stream.isEmpty()) {
                throw new RuntimeException("O paciente " + patientId + " não possui nenhum documento " + documentId);
            }

            // Precisa fazer o método de remover para poder chamar nessa parte
          //  minioMedicalDocumentStorage.deleteFile(patientId, stream);
  
            } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar o documento: " + e.getMessage(), e);
        }
    }

    private void validateBucket(String bucket) {
        if (!storageClient.bucketExists(bucket)) {
            throw new RuntimeException("Bucket não existe: " + bucket);
        }
    }


}
package br.org.apae.documentos_medicos.application;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.api.dto.responses.PatientPresignedUrlsDTO;
import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_medicos.application.exceptions.MedicalDocumentServiceException;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@Service
public class MedicalDocumentServiceImpl implements MedicalDocumentService {

    private MinioStorage minioMedicalDocumentStorage;
    private StorageClient storageClient;
    private static final String FOLDER_NAME = "documentos-medicos";
    private static final int EXPIRATION_TIME_HOURS = 2;

    @Autowired
    public MedicalDocumentServiceImpl(MinioStorage minioMedicalDocumentStorage, StorageClient storageClient) {
        this.minioMedicalDocumentStorage = minioMedicalDocumentStorage;
        this.storageClient = storageClient;
    }

    @Override
    public void saveFile(MedicalDocumentUploadDTO dtoObject, MultipartFile multipartFile) {
        validateBucket(dtoObject.getPatientId());

        if (multipartFile.isEmpty()) {
            throw new FileIsEmptyException("Não é possivel fazer upload de um arquivo vazio.");
        }

        try {
            String bucket = dtoObject.getPatientId();
            String path = FOLDER_NAME + "/" + 
                            dtoObject.getYear() + "/" +
                            MedcialDocumentType.valueOf(dtoObject.getDocumentType()).getPrefix() + "/" + 
                            multipartFile.getOriginalFilename();
                            
            byte[] file = multipartFile.getBytes();

            minioMedicalDocumentStorage.uploadFile(bucket, path, file);

        } catch (IOException e) {
            throw new MedicalDocumentServiceException(
                "Erro ao processar o arquivo: " + e.getMessage()
            );
        }
    }

    @Override
    public MedicalDocumentResponseDTO listMedicalDocument(String patientId, Integer year) {
        validateBucket(patientId);
        try {
            String prefix = FOLDER_NAME;
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);

            if (objectNames.isEmpty()) {
                throw new DocumentNotFoundException(
                    "Nenhum documento encontrado para o paciente " + patientId + " no ano " + year
                );
            }

            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(
                patientId, objectNames, 
                EXPIRATION_TIME_HOURS);

            List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(objectNames, presignedUrls);

            return new MedicalDocumentResponseDTO(
                patientId, 
                files
            );
        } catch (Exception e) {
            throw new MedicalDocumentServiceException(
                "Erro ao listar documentos: " + e.getMessage()
            );
        }
    }
    
    @Override
    public MedicalDocumentResponseDTO listMedicalDocumentByType(String patientId, Integer year, MedcialDocumentType type) {
        validateBucket(patientId);
        try {
            String prefix = FOLDER_NAME + "/" + year + "/"+ type.getPrefix() + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);
            
            if (objectNames.isEmpty()) {
                throw new DocumentNotFoundException(
                    "Nenhum documento encontrado para o paciente " + patientId + " do' tipo " + type
                );
            }
            
            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(
                patientId, objectNames, 
                EXPIRATION_TIME_HOURS);

            List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(objectNames, presignedUrls);

            return new MedicalDocumentResponseDTO(
                patientId, 
                files
            );      
        } catch (Exception e) {
            throw new MedicalDocumentServiceException(
                "Erro ao listar documentos do tipo: " + e.getMessage()
            );
        }
    }
            
    @Override
    public MedicalDocumentResponseDTO getDocumentHistoryByType(String patientId, MedcialDocumentType type) {
        validateBucket(patientId);
        try {
            String prefix = FOLDER_NAME + "/";
            List<String> objectNames = minioMedicalDocumentStorage.listObject(patientId, prefix);
            
            if (objectNames.isEmpty()) {
                throw new DocumentNotFoundException(
                    "Nenhum histórico encontrado para o paciente " + patientId + " do tipo " + type
                );
            }
                    
            List<String> filteredObjectNames = objectNames.stream()
            .filter(objectName -> objectName.contains(type.getPrefix()))
            .toList();
                    
            List<String> presignedUrls = minioMedicalDocumentStorage.generatePresignedUrls(
                patientId, 
                filteredObjectNames, 
                EXPIRATION_TIME_HOURS);
                    
            List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(
                filteredObjectNames, 
                presignedUrls);

            return new MedicalDocumentResponseDTO(
                patientId, 
                files
            );   
        } catch (Exception e) {
            throw new MedicalDocumentServiceException(
                "Erro ao buscar histórico de documentos: " + e.getMessage()
            );
        }
    }
        
        
    @Override
    public byte[] viewPatientMedicalDocuments(String patientId, String path) {
        validateBucket(patientId);
        try {
            byte[] file = minioMedicalDocumentStorage.getMedicalDocumentByFileName(patientId, path);
            
            if (file == null || file.length == 0) {
                throw new DocumentNotFoundException("Documento não encontrado.");
            }
            
            return file;
        } catch (Exception e) {
            throw new MedicalDocumentServiceException(
                "Erro ao visualizar o documento: " + e.getMessage()
            );
        }
    }
    
    @Override
    public void deleteDocument(String patientId, String fileName) {
        validateBucket(patientId);
        minioMedicalDocumentStorage.deleteFile(patientId, fileName);   
    }

    private void validateBucket(String bucket) {
        if (!storageClient.bucketExists(bucket)) {
            throw new BucketNotFoundException("Bucket não existe: " + bucket);
        }
    }

    private List<PatientPresignedUrlsDTO> getPatientPresignedUrlsDto(List<String> objectNames, List<String> presignedUrls) {
        List<PatientPresignedUrlsDTO> files = objectNames.stream()
            .map(objectName -> {
                String presignedUrl = presignedUrls.get(objectNames.indexOf(objectName));
                return new PatientPresignedUrlsDTO(objectName, presignedUrl);
            })
            .toList();
        return files;
    }
}
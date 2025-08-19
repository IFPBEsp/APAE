package br.org.apae.documentos_digitalizados.application;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_digitalizados.api.dto.DocumentObjectRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.DocumentsResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.PatientPresignedUrlsDTO;
import br.org.apae.documentos_digitalizados.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_digitalizados.application.exceptions.DocumentServiceException;
import br.org.apae.documentos_digitalizados.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_digitalizados.domain.DocumentCategory;
import br.org.apae.documentos_digitalizados.domain.DocumentType;
import br.org.apae.documentos_digitalizados.infrastructure.storage.IStorageService;

@Service
public class DigitizedDocumentService {
  private final IStorageService storageService;
  private static final int EXPIRATION_TIME_HOURS = 2;

  @Autowired
  public DigitizedDocumentService(IStorageService service) {
    this.storageService = service;
  }

  public void createNewBucket(UUID bucketName) {
    validateBucketIfExist(bucketName);
    storageService.createBucket(bucketName.toString());
  }

  public void removeBucket(UUID bucketName) {
    validateBucketIfNotExist(bucketName);
    storageService.deleteBucket(bucketName.toString());
  }

  public void saveFile(DocumentObjectRequestDTO dtoObject, MultipartFile multipartFile) {
    validateBucketIfNotExist(UUID.fromString(dtoObject.patientId()));
    dtoObject.validateDocument();

    if (multipartFile.isEmpty() || multipartFile.getSize() == 0) {
        throw new FileIsEmptyException("Não é possivel fazer upload de um arquivo vazio.");
    }

    try {
        String bucket = dtoObject.patientId();
        String path = "%s/%d/%s/%s".formatted(
          DocumentCategory.valueOf(dtoObject.documentCategory()).getCategory(),
          dtoObject.year(),
          dtoObject.documentType(),
          multipartFile.getOriginalFilename()
        );
        byte[] file = multipartFile.getBytes();
        storageService.uploadFile(bucket, path, file);
    } catch (IOException e) {
        throw new DocumentServiceException(
            "Erro ao processar o arquivo: " + e.getMessage()
        );
    }
  }

  public DocumentsResponseDTO listDocument(UUID patientId, String documentCategory, Integer year) {
    validateBucketIfNotExist(patientId);

    String prefix = "%s/%d/".formatted(
      DocumentCategory.valueOf(documentCategory.toUpperCase()).getCategory(),
      year
    );
    List<String> objectNames = storageService.listObject(patientId.toString(), prefix);

    if (objectNames.isEmpty()) {
      throw new DocumentNotFoundException(
        "Nenhum documento encontrado para o paciente " 
        + patientId.toString() 
        + " no ano " + year
      );
    }

    List<String> presignedUrls = storageService.generatePresignedUrls(
      patientId.toString(), 
      objectNames, 
      EXPIRATION_TIME_HOURS
    );

    List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(objectNames, presignedUrls);

    return new DocumentsResponseDTO(
      patientId.toString(), 
      files
    );
  }

  public DocumentsResponseDTO listDocumentByType(UUID patientId, Integer year, String category, String type) {
    validateBucketIfNotExist(patientId);

    String prefix = "%s/%d/%s/".formatted(
      DocumentCategory.valueOf(category.toUpperCase()).getCategory(),
      year,
      DocumentType.valueOf(type.toUpperCase()).getType()
    );
    List<String> objectNames = storageService.listObject(patientId.toString(), prefix);
        
    if (objectNames.isEmpty()) {
      throw new DocumentNotFoundException(
        "Nenhum documento encontrado para o paciente "
        + patientId.toString() 
        + " do' tipo " + type
        + " no ano " + year
      );
    }
        
    List<String> presignedUrls = storageService.generatePresignedUrls(
      patientId.toString(), 
      objectNames, 
      EXPIRATION_TIME_HOURS
    );
    List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(
      objectNames,
      presignedUrls
    );
    
    return new DocumentsResponseDTO(
      patientId.toString(), 
      files
    );      
  }

  public DocumentsResponseDTO getDocumentHistoryByType(UUID patientId, String category, String type) {
    validateBucketIfNotExist(patientId);
    
    String prefix = DocumentCategory.valueOf(category).getCategory() + "/";
    List<String> objectNames = storageService.listObject(patientId.toString(), prefix);
        
    if (objectNames.isEmpty()) {
      throw new DocumentNotFoundException("Nenhum documento encontrado.");
    }
                
    List<String> filteredObjectNames = getFilteredObjectNames(type, objectNames);

     if (filteredObjectNames.isEmpty()) {
        throw new DocumentNotFoundException(
            "Nenhum documento foi encontrado para o paciente " + patientId.toString() + " do tipo " + type
        );
    }
                
    List<String> presignedUrls = storageService.generatePresignedUrls(
      patientId.toString(), 
      filteredObjectNames, 
      EXPIRATION_TIME_HOURS
    );
                
    List<PatientPresignedUrlsDTO> files = getPatientPresignedUrlsDto(
      filteredObjectNames, 
      presignedUrls
    );
    
    return new DocumentsResponseDTO(
        patientId.toString(), 
        files
    );   
  }

  public byte[] viewPatientMedicalDocuments(UUID patientId, String path) {
    validateBucketIfNotExist(patientId);
    
    byte[] file = storageService.getDocumentByFileName(patientId.toString(), path);
    
    if (file == null || file.length == 0) {
        throw new DocumentNotFoundException("Documento não encontrado.");
    }

    return file;
  }

  public void deleteDocument(UUID patientId, String fileName) {
    validateBucketIfNotExist(patientId);
    storageService.deleteFile(patientId.toString(), fileName);   
  }

  private void validateBucketIfExist(UUID bucketName) {
    if (storageService.isBucketExist(bucketName.toString())) {
      throw new RuntimeException("Bucket já existe com esse ID");
    }
  }

  private void validateBucketIfNotExist(UUID bucketName) {
    if (!storageService.isBucketExist(bucketName.toString())) {
      throw new RuntimeException("Nenhum bucket foi encontrado com esse ID.");
    }
  }
  
  private List<PatientPresignedUrlsDTO> getPatientPresignedUrlsDto(List<String> objectNames, List<String> presignedUrls) {
    List<PatientPresignedUrlsDTO> files = objectNames.stream()
      .map(objectName -> {
        String presignedUrl = presignedUrls.get(objectNames.indexOf(objectName));
        return new PatientPresignedUrlsDTO(objectName, presignedUrl);
      }
    ).toList();

      return files;
  }

  private List<String> getFilteredObjectNames(String type, List<String> objectNames) {
    List<String> filteredObjectNames = objectNames.stream()
      .filter(
        objectName -> objectName.contains(
        DocumentType.valueOf(type).getType()
      )
    ).toList();

    return filteredObjectNames;
  }
}

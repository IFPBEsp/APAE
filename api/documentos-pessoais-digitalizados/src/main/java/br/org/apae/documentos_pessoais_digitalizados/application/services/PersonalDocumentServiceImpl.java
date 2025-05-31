package br.org.apae.documentos_pessoais_digitalizados.application.services;

import java.io.IOException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentUrlReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_pessoais_digitalizados.application.exceptions.PersonalDocumentServiceException;
import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocumentType;
import br.org.apae.documentos_pessoais_digitalizados.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_pessoais_digitalizados.infrastructure.client.StorageClient;

@Service
public class PersonalDocumentServiceImpl implements PersonalDocumentService {
    private final MinioStorage  minioStorage;
    private final String        folderName;
    private final Integer       expiration_time_hours;
    private final StorageClient storageClient;

    @Autowired
    public PersonalDocumentServiceImpl(MinioStorage minioStorage, @Value("${minio.folder.name}") String folderName,
                                       @Value("${minio.expiration.time}") Integer expirationTimeHours,
                                       StorageClient storageClient) {
        this.minioStorage     = minioStorage;
        this.folderName       = folderName;
        expiration_time_hours = expirationTimeHours;
        this.storageClient    = storageClient;
    }

    @Override
    public void deleteDocument(String patientId, String fileName) {
        verifyBucket(patientId);
        this.minioStorage.deleteFile(patientId, fileName);
    }

    @Override
    public PersonalDocumentResDTO listPersonalDocument(String patientId) {
        verifyBucket(patientId);

        try {
            List<String> objectsName = this.minioStorage.listObject(patientId, this.folderName);

            if (objectsName.isEmpty()) {
                throw new DocumentNotFoundException("Nenhum documento encontrado para o paciente " + patientId);
            }

            List<String> presignedUrls = this.minioStorage.generatePresignedUrls(patientId,
                                                                                 objectsName,
                                                                                 this.expiration_time_hours);

            return new PersonalDocumentResDTO(patientId, getPersonalPresignedUrls(objectsName, presignedUrls));
        } catch (Exception e) {
            throw new PersonalDocumentServiceException("Erro ao listar documentos: " + e.getMessage());
        }
    }

    @Override
    public PersonalDocumentResDTO listPersonalDocumentByType(String patientId, PersonalDocumentType type) {
        verifyBucket(patientId);

        try {
            List<String> objectNames = this.minioStorage.listObject(patientId,
                                                                    this.folderName + "/" + type.getPrefix() + "/");

            if (objectNames.isEmpty()) {
                throw new DocumentNotFoundException("Nenhum documento encontrado para o paciente " + patientId
                                                    + " do tipo " + type);
            }

            List<String> filteredObjectNames = objectNames.stream()
                                                          .filter(objectName -> objectName.contains(type.getPrefix()))
                                                          .toList();
            List<String> presignedUrls = this.minioStorage.generatePresignedUrls(patientId,
                                                                                 filteredObjectNames,
                                                                                 this.expiration_time_hours);

            return new PersonalDocumentResDTO(patientId, getPersonalPresignedUrls(filteredObjectNames, presignedUrls));
        } catch (Exception e) {
            throw new PersonalDocumentServiceException("Erro ao listar documentos do tipo: " + e.getMessage());
        }
    }

    @Override
    public void saveFile(PersonalDocumentReqDTO personalDTO) {
        MultipartFile file = personalDTO.file();

        if (file.isEmpty()) {
            throw new FileIsEmptyException("Não é possivel fazer upload de um arquivo vazio.");
        }

        String bucket = personalDTO.patientId();

        this.storageClient.makeBucket(bucket);

        try {
            String path = this.folderName + "/" + PersonalDocumentType.valueOf(personalDTO.documentType()).getPrefix()
                          + "/" + file.getOriginalFilename();

            this.minioStorage.uploadFile(bucket, path, file.getBytes());
        } catch (IOException e) {
            throw new PersonalDocumentServiceException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    private void verifyBucket(String bucketName) {
        if (!this.storageClient.bucketExists(bucketName)) {
            throw new BucketNotFoundException("Bucket não existe: " + bucketName);
        }
    }

    @Override
    public byte[] viewPatientPersonalDocuments(String patientId, String path) {
        verifyBucket(patientId);

        try {
            byte[] file = this.minioStorage.getPersonalDocumentByFileName(patientId, path);

            if ((file == null) || (file.length == 0)) {
                throw new DocumentNotFoundException("Documento não encontrado.");
            }

            return file;
        } catch (Exception e) {
            throw new PersonalDocumentServiceException("Erro ao visualizar o documento: " + e.getMessage());
        }
    }

    private List<PersonalDocumentUrlReqDTO> getPersonalPresignedUrls(List<String> objectNames,
                                                                     List<String> presignedUrls) {
        return objectNames.stream().map(objectName -> {
                String presignedUrl = presignedUrls.get(objectNames.indexOf(objectName));

                return new PersonalDocumentUrlReqDTO(objectName, presignedUrl);
            })            .toList();
    }

}

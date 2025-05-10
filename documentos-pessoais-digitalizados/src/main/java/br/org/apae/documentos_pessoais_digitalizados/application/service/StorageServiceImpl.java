package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.exception.StorageException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.messages.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class StorageServiceImpl implements StorageService {

    private final MinioClient minioClient;
    private final String folderName;

    @Autowired
    public StorageServiceImpl(@Value("${minio.folder.name}") String folderName, MinioClient minioClient) {
        this.folderName = folderName;
        this.minioClient = minioClient;
    }

    @Override
    public byte[] findDocumentByFileName(String fileName, String bucketName) throws FileNotFoundException {
        try (InputStream stream = this.minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {

            return stream.readAllBytes();
        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    @Override
    public String uploadDocuments(PersonalDocumentFileReqDTO personalDocument, String bucketName) {
        Map<String, String> tags = new HashMap<>();
        tags.put("documentType", personalDocument.personalDocumentType().name());
        Tags objectTags = Tags.newObjectTags(tags);
        MultipartFile file = personalDocument.file();
        try (InputStream inputStream = file.getInputStream()) {
            String pathOfFile = this.folderName + "/" + file.getOriginalFilename();
            this.minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(pathOfFile)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(file.getContentType())
                    .tags(objectTags)
                    .build());
            return pathOfFile;
        } catch (Exception e) {
            throw new StorageException();
        }
    }
}

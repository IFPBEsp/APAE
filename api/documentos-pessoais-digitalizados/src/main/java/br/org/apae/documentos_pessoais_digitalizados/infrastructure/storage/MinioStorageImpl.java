package br.org.apae.documentos_pessoais_digitalizados.infrastructure.storage;

import br.org.apae.documentos_pessoais_digitalizados.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_pessoais_digitalizados.infrastructure.storage.exceptions.PersonalDocumentStorageException;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class MinioStorageImpl implements MinioStorage {

    private final MinioClient minioClient;

    @Autowired
    public MinioStorageImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void uploadFile(String bucket, String path, byte[] file) {
        try {
            InputStream fileStream = new ByteArrayInputStream(file);
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(path)
                            .stream(fileStream, file.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (Exception e) {
            throw new PersonalDocumentStorageException("Erro ao fazer upload do arquivo: " + e.getMessage());
        }

    }

    @Override
    public List<String> listObject(String bucket, String prefix) {
        List<String> objectNames = new ArrayList<>();
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                objectNames.add(item.objectName());
            }
            return objectNames;
        } catch (Exception e) {
            throw new PersonalDocumentStorageException("Erro ao listar objetos: " + e.getMessage());
        }
    }

    @Override
    public byte[] getPersonalDocumentByFileName(String bucket, String objectName) {
        try {
            InputStream inputStream = this.minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );

            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new PersonalDocumentStorageException("Erro ao obter o arquivo: " + e.getMessage());
        }
    }


    @Override
    public List<String> generatePresignedUrls(String bucket, List<String> objectNames, int expirationTimeInHours) {
            return objectNames.stream().map(name -> {
                try {
                    return this.minioClient.getPresignedObjectUrl(
                            GetPresignedObjectUrlArgs.builder()
                                    .method(Method.GET)
                                    .bucket(bucket)
                                    .object(name)
                                    .expiry(expirationTimeInHours * 3600)
                                    .build()
                    );
                } catch (Exception e) {
                    throw new PersonalDocumentStorageException("Erro ao gerar URL pré-assinada: " + e.getMessage());
                }
            }).toList();
    }

    @Override
    public void deleteFile(String bucket, String objectName) {
        try {
            this.minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new PersonalDocumentStorageException("Erro ao deletar o arquivo: " + e.getMessage());
        }
    }
}

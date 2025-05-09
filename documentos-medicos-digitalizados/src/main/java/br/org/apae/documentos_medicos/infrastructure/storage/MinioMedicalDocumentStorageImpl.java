package br.org.apae.documentos_medicos.infrastructure.storage;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import io.minio.GetObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;

public class MinioMedicalDocumentStorageImpl implements MinioStorage {

    private final MinioClient minioClient;

    @Autowired
    public MinioMedicalDocumentStorageImpl(MinioClient minioClient) {
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
            throw new RuntimeException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
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
                    .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                objectNames.add(item.objectName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar objetos: " + e.getMessage(), e);
        }
        return objectNames;
    }

    @Override
    public byte[] getMedicalDocumentByFileName(String bucket, String objectName) {
        try {
            InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
            );

            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter o arquivo: " + e.getMessage(), e);
        }
    }
}    

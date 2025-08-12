package br.org.apae.documentos_digitalizados.infrastructure.storage.minIO;

import br.org.apae.documentos_digitalizados.infrastructure.storage.IStorageService;
import br.org.apae.documentos_digitalizados.infrastructure.storage.exceptions.MinIOHandleException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Item;

@Component
public class MinIOStorageServiceImpl implements IStorageService {
  private MinioClient minioClient;

    @Autowired
    public MinIOStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void createBucket(String bucketName) {
        try {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                .bucket(bucketName)
                .build()
            );
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao criar o bucket: " + e.getMessage());
        }
    }

    @Override
    public boolean isBucketExist(String bucketName) {
        try {
            return minioClient.bucketExists(
                BucketExistsArgs.builder()
                .bucket(bucketName).
                build()
            );
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao validar se o bucket existe: " + e.getMessage());
        }
    }

    @Override 
    public void deleteBucket(String bucketName) {
        try {
            minioClient.removeBucket(
                RemoveBucketArgs.builder()
                .bucket(bucketName)
                .build());
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao deletar o bucket: " + e.getMessage());
        }
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
            throw new MinIOHandleException("Erro ao fazer upload do arquivo: " + e.getMessage());
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
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao listar objetos: " + e.getMessage());
        }
        return objectNames;
    }

    @Override
    public byte[] getDocumentByFileName(String bucket, String objectName) {
        try {
            InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
            );

            return inputStream.readAllBytes();
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao obter o arquivo: " + e.getMessage());
        }
    }

    @Override
    public List<String> generatePresignedUrls(String bucket, List<String> objectNames, int expirationTimeInHours) {
        try {
            List<String> urls = new ArrayList<>();
            int expirationTimeInSeconds = expirationTimeInHours * 3600;
            for (String objectName : objectNames) {
                String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(objectName)
                        .expiry(expirationTimeInSeconds)
                        .build()
                );
                urls.add(url);
            }
            return urls;
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao gerar URL pré-assinada: " + e.getMessage());
        }
    }

    @Override
    public void deleteFile(String bucket, String objectName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            throw new MinIOHandleException("Erro ao deletar o arquivo: " + e.getMessage());
        }
    }
}

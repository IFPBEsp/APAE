package br.org.apae.documentos_digitalizados.application.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.buckets.laudos}")
    private String bucketLaudos;

    @Value("${minio.buckets.encaminhamentos}")
    private String bucketEncaminhamentos;

    @Override
    public void uploadLaudo(UUID UUIDLaudo, MultipartFile file) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketLaudos)
                        .object(UUIDLaudo.toString())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    @Override
    public void uploadEncaminhamento(UUID UUIDEncaminhamento, MultipartFile file) throws Exception {
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketEncaminhamentos)
                        .object(UUIDEncaminhamento.toString())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );
    }

    @Override
    public InputStream downloadLaudo(UUID UUIDLaudo) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketLaudos)
                        .object(UUIDLaudo.toString())
                        .build()
        );
    }

    @Override
    public InputStream downloadEncaminhamento(UUID UUIDEncaminhamento) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketEncaminhamentos)
                        .object(UUIDEncaminhamento.toString())
                        .build()
        );
    }
}

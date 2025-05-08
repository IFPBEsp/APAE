package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.exception.DocumentoStorageException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Override
    public void criarBucket(String bucketNome)  {
        boolean exists;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketNome).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (!exists) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketNome).build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Override
    public StatObjectResponse metadadoDocumento(String bucketNome, String documentoCaminho) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(documentoCaminho)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha no busca do metadado do documento\n" + e);
        }
    }

}

package br.org.apae.documentos_digitalizados.application.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
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
    public void listarBuckets() {

    }

    @Override
    public void listarBucketPorNome(String bucketNome) {

    }

    @Override
    public void atualizarBucket(String bucketNome) {

    }

    @Override
    public void deletarBucket(String bucketNome) {

    }
}

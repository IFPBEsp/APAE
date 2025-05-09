package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.domain.exception.DiretorioException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Override
    public void criarBucket(UUID bucketNome) {
        boolean existe = existeBucket(bucketNome.toString());

        if (!existe) {
            try {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketNome.toString()).build());
                criarPasta(bucketNome.toString(), "documentos-pessoal/");
                criarPasta(bucketNome.toString(), "documentos-medico/");
                criarPasta(bucketNome.toString(), "documentos-escolar/");
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
    public void deletarBucket(String bucketNome) {

    }

    private boolean existeBucket(String bucketNome) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketNome).build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void criarPasta(String bucketNome, String nomePasta){
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(nomePasta)
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .build()
            );
        } catch (Exception e){
            throw new DiretorioException("Falha ao criar diretório!\n" + e);
        }
    }
}

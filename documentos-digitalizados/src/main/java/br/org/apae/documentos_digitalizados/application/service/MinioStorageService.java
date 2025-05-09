package br.org.apae.documentos_digitalizados.application.service;

import java.util.UUID;

public interface MinioStorageService {
    void criarBucket(UUID bucketNome);
    void listarBuckets();
    void listarBucketPorNome(String bucketNome);
    void deletarBucket(String bucketNome);
}

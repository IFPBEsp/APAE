package br.org.apae.documentos_digitalizados.application.service;

public interface MinioStorageService {
    void criarBucket(String bucketNome);
    void listarBuckets();
    void listarBucketPorNome(String bucketNome);
    void atualizarBucket(String bucketNome);
    void deletarBucket(String bucketNome);
}

package br.org.apae.documentos_digitalizados.application.service;
import io.minio.StatObjectResponse;


public interface MinioStorageService {
    void criarBucket(String bucketNome);
    StatObjectResponse metadadoDocumento(String bucketNome, String documentoCaminho);

}

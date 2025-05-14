package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.api.dto.BucketResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;

import java.util.UUID;

public interface MinioStorageService {
    void criarBucket(UUID bucketNome);
    ListagemBucketResponseDTO listarBuckets();
    BucketResponseDTO listarBucketPorNome(String bucketNome);
    void deletarBucket(String bucketNome);
    boolean existeBucket(String bucketNome);
}

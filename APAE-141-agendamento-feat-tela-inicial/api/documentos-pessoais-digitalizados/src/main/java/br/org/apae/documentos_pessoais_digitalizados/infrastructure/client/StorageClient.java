package br.org.apae.documentos_pessoais_digitalizados.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StorageClient {
    private final RestClient restClient;

    public StorageClient(@Value("${client.storage.endpoint.url}") String url) {
        this.restClient = RestClient.builder().baseUrl(url).build();
    }

    public boolean bucketExists(String bucketName) {
        return Boolean.TRUE.equals(this.restClient.get()
                                                  .uri("/verificar/{bucketName}", bucketName)
                                                  .retrieve()
                                                  .body(Boolean.class));
    }

    public void makeBucket(String bucketName) {
        this.restClient.post().uri("/{bucketName}", bucketName);
    }
}

package br.org.apae.documentos_medicos.infrastructure.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StorageClient {
    private final RestClient restClient;

    public StorageClient(RestClient restClient, String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    public boolean bucketExists(String bucketName) {
        return this.restClient.get()
                .uri("/verificar/{bucketName}", bucketName)
                .retrieve()
                .body(Boolean.class);
    }
}

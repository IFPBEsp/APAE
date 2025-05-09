package br.org.apae.documentos_pessoais_digitalizados.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class StorageClient {
    private final RestClient restClient;

    public StorageClient(RestClient restClient, @Value("${storage.service.base.url}") String url) {
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    public void makeBucket(String bucketName) {
        this.restClient.post()
                .uri("/{bucketName}", bucketName);
    }
}

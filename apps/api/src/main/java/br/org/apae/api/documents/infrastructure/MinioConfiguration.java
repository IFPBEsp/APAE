package br.org.apae.api.documents.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.minio.MinioClient;

@Configuration
public class MinioConfiguration {
    @Value("${minio.url}")
    private String url;

    @Value("${minio.public.url}")
    private String publicUrl;

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String accessSecret;

    @Bean
    @Primary
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, accessSecret)
                .region("us-east-1")
                .build();
    }

    @Bean(name = "minioPublicClient")
    public MinioClient minioPublicClient() {
        return MinioClient.builder()
                .endpoint(publicUrl)
                .credentials(accessKey, accessSecret)
                .region("us-east-1")
                .build();
    }
}

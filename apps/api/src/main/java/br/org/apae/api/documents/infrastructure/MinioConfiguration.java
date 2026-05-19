package br.org.apae.api.documents.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.minio.MinioClient;

@Configuration
public class MinioConfiguration {
    private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

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

    @Bean
    public ApplicationRunner minioConnectionCheck(MinioClient minioClient) {
        return args -> {
            try {
                minioClient.listBuckets();
                log.info("Conexão com MinIO validada em {}", url);
            } catch (Exception e) {
                throw new IllegalStateException(
                        "Falha ao conectar no MinIO em " + url + ". "
                                + "Confira se o serviço está de pé e se MINIO_URL/MINIO_ACCESS_KEY/MINIO_SECRET_KEY estão corretos. "
                                + "Causa: " + e.getClass().getSimpleName() + " - " + e.getMessage(),
                        e);
            }
        };
    }
}

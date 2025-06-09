package br.org.apae.documentos_digitalizados.infrastructure.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API documentos digitalizados")
                        .version("1.0")
                        .description("API tem a responsabilidade de criar a estrutura de buckets de cada paciênte da APAE no minIO."));
    }
}

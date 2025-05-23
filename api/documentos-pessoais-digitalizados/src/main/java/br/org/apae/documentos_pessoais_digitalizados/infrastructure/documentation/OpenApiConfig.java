package br.org.apae.documentos_pessoais_digitalizados.infrastructure.documentation;

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
                .title("API documentos pessoais digitalizados")
                .version("1.0")
                .description("API tem a responsabilidade de salvar documentos pessoais de um determinado paciente "));
    }
}
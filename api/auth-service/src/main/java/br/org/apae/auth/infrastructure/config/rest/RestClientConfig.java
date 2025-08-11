package br.org.apae.auth.infrastructure.config.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  
  @Bean
  public RestClient restClient() {
    RestClient restClient = RestClient.builder().build();

    return restClient;
  }

}

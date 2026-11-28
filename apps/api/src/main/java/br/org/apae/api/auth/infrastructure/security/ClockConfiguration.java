package br.org.apae.api.auth.infrastructure.security;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {
  @Bean
  public Clock clock() {
    return Clock.system(ZoneId.systemDefault());
  }
}

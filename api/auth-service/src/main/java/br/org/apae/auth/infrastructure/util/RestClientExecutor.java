package br.org.apae.auth.infrastructure.util;

import java.util.function.Supplier;

import org.springframework.web.client.RestClientException;

import br.org.apae.auth.infrastructure.util.exceptions.ExternalServiceException;

public class RestClientExecutor {
  public static <T> T execute(Supplier<T> action, String description) {
    try {
      T result =  action.get();
      return result;
    } catch (RestClientException e) {
      throw new ExternalServiceException("Erro na comunicação com serviço externo: " + description +"\n" + e);
    } catch (Exception e) {
      throw new ExternalServiceException("Erro inesperado ao executar ação: " + description + "\n" + e);
    }
  }

  public static void execute(Runnable action, String description) {
    try {
      action.run();
    } catch (RestClientException e) {
      throw new ExternalServiceException("Erro na comunicação com serviço externo: " + description +"\n" + e);
    } catch (Exception e) {
      throw new ExternalServiceException("Erro inesperado ao executar ação: " + description + "\n" + e);
    }
  }
}

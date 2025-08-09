package br.org.apae.documentos_digitalizados.infrastructure.storage.exceptions;

public class MinIOHandleException extends RuntimeException {
  
  public MinIOHandleException(String message) {
    super(message);
  }
}

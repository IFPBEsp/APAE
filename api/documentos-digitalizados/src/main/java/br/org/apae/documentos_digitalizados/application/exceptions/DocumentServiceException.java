package br.org.apae.documentos_digitalizados.application.exceptions;

public class DocumentServiceException extends RuntimeException {
    public DocumentServiceException(String message) {
        super(message);
    }

    public DocumentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
  
}

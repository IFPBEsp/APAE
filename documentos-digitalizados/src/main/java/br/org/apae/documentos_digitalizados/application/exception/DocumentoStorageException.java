package br.org.apae.documentos_digitalizados.application.exception;

public class DocumentoStorageException extends RuntimeException {
    public DocumentoStorageException(String message) {
        super(message);
    }
}

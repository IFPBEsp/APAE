package br.org.apae.documentos_escolares.application.service.exceptions;

public class DocumentoDuplicadoException extends RuntimeException {
    public DocumentoDuplicadoException(String message) {
        super(message);
    }
}

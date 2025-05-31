package br.org.apae.documentos_pessoais_digitalizados.application.exceptions;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super(message);
    }
}

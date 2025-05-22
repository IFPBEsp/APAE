package br.org.apae.documentos_pessoais_digitalizados.application.exceptions;

public class PersonalDocumentServiceException extends RuntimeException {
    public PersonalDocumentServiceException(String message) {
        super(message);
    }
}

package br.org.apae.documentos_pessoais_digitalizados.application.exceptions;

public class BucketNotFoundException extends RuntimeException {
    public BucketNotFoundException(String message) {
        super(message);
    }
}

package br.org.apae.documentos_digitalizados.domain.exception;

public class ExisteBucketException extends RuntimeException {
    public ExisteBucketException(String message) {
        super(message);
    }
}

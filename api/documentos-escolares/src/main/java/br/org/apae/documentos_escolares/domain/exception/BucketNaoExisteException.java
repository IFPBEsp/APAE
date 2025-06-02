package br.org.apae.documentos_escolares.domain.exception;

public class BucketNaoExisteException extends RuntimeException {
    public BucketNaoExisteException(String message) {
        super(message);
    }
}

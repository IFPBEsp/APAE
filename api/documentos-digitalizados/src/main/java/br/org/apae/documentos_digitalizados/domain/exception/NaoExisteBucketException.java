package br.org.apae.documentos_digitalizados.domain.exception;

public class NaoExisteBucketException extends RuntimeException {
    public NaoExisteBucketException(String message) {
        super(message);
    }
}

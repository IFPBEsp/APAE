package br.org.apae.documentos_escolares.domain.exception;

public class ArquivoVazioException extends RuntimeException {
    public ArquivoVazioException(String message) {
        super(message);
    }
}

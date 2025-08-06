package br.org.apae.documentos_escolares.domain.exception;

public class ErroAoDeletarDocumentoException extends RuntimeException {
    public ErroAoDeletarDocumentoException(String message) {
        super(message);
    }
}

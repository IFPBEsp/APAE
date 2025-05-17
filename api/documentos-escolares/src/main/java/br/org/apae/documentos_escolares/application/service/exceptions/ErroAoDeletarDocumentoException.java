package br.org.apae.documentos_escolares.application.service.exceptions;

public class ErroAoDeletarDocumentoException extends RuntimeException {
    public ErroAoDeletarDocumentoException(String message) {
        super(message);
    }
}

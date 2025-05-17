package br.org.apae.documentos_escolares.application.service.exceptions;

public class ErroAoSalvarDocumentoException extends RuntimeException {
    public ErroAoSalvarDocumentoException(String message) {
        super(message);
    }
}

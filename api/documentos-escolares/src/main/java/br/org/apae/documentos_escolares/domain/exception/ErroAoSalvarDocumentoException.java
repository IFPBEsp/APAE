package br.org.apae.documentos_escolares.domain.exception;

public class ErroAoSalvarDocumentoException extends RuntimeException {
    public ErroAoSalvarDocumentoException(String message) {
        super(message);
    }
}

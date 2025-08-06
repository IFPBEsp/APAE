package br.org.apae.documentos_escolares.domain.exception;

public class DocumentoEscolarNaoEncontradoException extends RuntimeException {
    public DocumentoEscolarNaoEncontradoException(String message) {
        super(message);
    }
}

package br.org.apae.documentos_escolares.application.service.exceptions;

public class DocumentoEscolarNaoEncontradoException extends RuntimeException {
    public DocumentoEscolarNaoEncontradoException(String message) {
        super(message);
    }
}

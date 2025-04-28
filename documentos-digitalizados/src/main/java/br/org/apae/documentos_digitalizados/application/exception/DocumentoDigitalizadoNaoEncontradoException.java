package br.org.apae.documentos_digitalizados.application.exception;

public class DocumentoDigitalizadoNaoEncontradoException extends RuntimeException {
    public DocumentoDigitalizadoNaoEncontradoException(String message) {
        super(message);
    }
}

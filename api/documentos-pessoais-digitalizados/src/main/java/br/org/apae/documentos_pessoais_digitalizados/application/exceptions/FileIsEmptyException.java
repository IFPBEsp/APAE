package br.org.apae.documentos_pessoais_digitalizados.application.exceptions;

public class FileIsEmptyException extends RuntimeException {
    public FileIsEmptyException(String message) {
        super(message);
    }
}

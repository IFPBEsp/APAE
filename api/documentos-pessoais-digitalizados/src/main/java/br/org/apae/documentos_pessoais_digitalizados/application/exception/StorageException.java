package br.org.apae.documentos_pessoais_digitalizados.application.exception;

public class StorageException extends RuntimeException {
    public StorageException() {
        super("Error ao fazer o upload do arquivo");
    }
}

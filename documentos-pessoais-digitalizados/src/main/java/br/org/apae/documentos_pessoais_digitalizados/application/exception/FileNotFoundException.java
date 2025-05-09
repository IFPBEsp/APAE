package br.org.apae.documentos_pessoais_digitalizados.application.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("Error ao fazer o upload do arquivo");
    }
}

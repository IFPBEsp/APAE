package br.org.apae.documentos_pessoais_digitalizados.application.exception;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException() {
        super("Documento não encontrado ou não existe");
    }
}

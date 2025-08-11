package br.org.apae.documentos_pessoais_digitalizados.infrastructure.storage.exceptions;

public class PersonalDocumentStorageException extends RuntimeException {
    public PersonalDocumentStorageException(String message) {
        super(message);
    }
}

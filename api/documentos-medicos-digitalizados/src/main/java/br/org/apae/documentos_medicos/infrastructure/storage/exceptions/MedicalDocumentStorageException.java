package br.org.apae.documentos_medicos.infrastructure.storage.exceptions;

public class MedicalDocumentStorageException extends RuntimeException {
    public MedicalDocumentStorageException(String message) {
        super(message);
    }
}

package br.org.apae.documentos_medicos.application.exceptions;

public class MedicalDocumentServiceException extends RuntimeException{
    public MedicalDocumentServiceException(String message) {
        super(message);
    }
}

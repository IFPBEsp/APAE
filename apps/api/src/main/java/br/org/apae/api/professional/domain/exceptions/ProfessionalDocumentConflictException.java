package br.org.apae.api.professional.domain.exceptions;

public class ProfessionalDocumentConflictException extends RuntimeException {
    private static final String MESSAGE = "Documento profissional já cadastrado.";

    public ProfessionalDocumentConflictException() {
        super(MESSAGE);
    }
}
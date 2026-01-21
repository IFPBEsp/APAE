package br.org.apae.api.professional.domain.exceptions;

public class IdentityDocumentConflictException extends RuntimeException {
    private static final String MESSAGE =  "O documento de identidade  já cadastrado.";
    public IdentityDocumentConflictException() {
        super(MESSAGE);
    }
}

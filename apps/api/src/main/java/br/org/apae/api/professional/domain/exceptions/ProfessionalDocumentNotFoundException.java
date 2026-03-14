package br.org.apae.api.professional.domain.exceptions;

public class ProfessionalDocumentNotFoundException extends RuntimeException {
    public ProfessionalDocumentNotFoundException() {
        super("Documento do profissional não encontrado.");
    }

    public ProfessionalDocumentNotFoundException(String message) {
        super(message);
    }
}

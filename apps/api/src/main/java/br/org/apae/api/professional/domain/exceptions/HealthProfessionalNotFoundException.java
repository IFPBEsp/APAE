package br.org.apae.api.professional.domain.exceptions;

public class HealthProfessionalNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Profissional não encontrado.";

    public HealthProfessionalNotFoundException() {
        super(MESSAGE);
    }
}
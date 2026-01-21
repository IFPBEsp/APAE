package br.org.apae.api.professional.domain.exceptions;

public class AvailabilityNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Disponibilidade não encontrada.";

    public AvailabilityNotFoundException() {
        super(MESSAGE);
    }
}

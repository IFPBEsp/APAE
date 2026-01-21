package br.org.apae.api.professional.domain.exceptions;

public class AvailabilityConflictException extends RuntimeException {
    private static final String MESSAGE = "Já existe uma disponibilidade cadastrada para este profissional, dia e turno.";

    public AvailabilityConflictException() {
        super(MESSAGE);
    }
}

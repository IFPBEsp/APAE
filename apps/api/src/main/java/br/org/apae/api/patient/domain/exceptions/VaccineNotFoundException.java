package br.org.apae.api.patient.domain.exceptions;

public class VaccineNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Vacina não encontrada.";

    public VaccineNotFoundException() {
        super(MESSAGE);
    }

    public VaccineNotFoundException(String name) {
        super(String.format("Não foi possível encontrar a vacina: %s", name));
    }
}
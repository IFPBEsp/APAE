package br.org.apae.api.patient.domain.exceptions;

public class VaccineInUseException extends RuntimeException {
    private static final String MESSAGE = "Não é possível excluir esta vacina, pois ela está vinculada a um ou mais pacientes.";

    public VaccineInUseException() {
        super(MESSAGE);
    }
}

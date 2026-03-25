package br.org.apae.api.patient.domain.exceptions;

public class DisorderInUseException extends RuntimeException {
    private static final String MESSAGE = "Não é possível excluir este transtorno, pois ele está vinculado a um ou mais pacientes.";

    public DisorderInUseException() {
        super(MESSAGE);
    }
}
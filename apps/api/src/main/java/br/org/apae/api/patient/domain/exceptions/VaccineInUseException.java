package br.org.apae.api.patient.domain.exceptions;

public class VaccineInUseException extends RuntimeException {
    public VaccineInUseException(String message) {
        super(message);
    }

    public VaccineInUseException() {
        super("A vacina não pode ser excluída pois está vinculada a um paciente.");
    }
}
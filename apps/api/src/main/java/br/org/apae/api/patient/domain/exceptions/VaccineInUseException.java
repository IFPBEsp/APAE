package br.org.apae.api.patient.domain.exceptions;

public class VaccineInUseException extends RuntimeException {
    public VaccineInUseException() {
        super("Não é possível excluir esta vacina pois ela está vinculada a um ou mais pacientes.");
    }
}
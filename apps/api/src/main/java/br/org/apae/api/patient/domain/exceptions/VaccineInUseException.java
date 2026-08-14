package br.org.apae.api.patient.domain.exceptions;

public class VaccineInUseException extends RuntimeException {
    public VaccineInUseException(String message) {
        super(message);
    }
}
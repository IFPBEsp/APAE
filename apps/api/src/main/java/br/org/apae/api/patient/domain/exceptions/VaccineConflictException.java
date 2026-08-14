package br.org.apae.api.patient.domain.exceptions;

public class VaccineConflictException extends RuntimeException {
    public VaccineConflictException(String message) {
        super(message);
    }
}
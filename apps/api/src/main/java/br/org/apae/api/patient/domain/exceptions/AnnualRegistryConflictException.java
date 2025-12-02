package br.org.apae.api.patient.domain.exceptions;

public class AnnualRegistryConflictException extends RuntimeException {

    public AnnualRegistryConflictException(Integer year) {
        super("Conflito: Um registro anual para o ano " + year + " já existe para este paciente.");
    }
}
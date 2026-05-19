package br.org.apae.api.patient.domain.exceptions;

import java.time.Year;

public class AnnualRegistryConflictException extends RuntimeException {

    public AnnualRegistryConflictException(Year year) {
        super("Conflito: Um registro anual para o ano " + year + " já existe para este paciente.");
    }
}
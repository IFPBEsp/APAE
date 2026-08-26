package br.org.apae.api.patient.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class VaccineConflictException extends RuntimeException {

    private static final String MESSAGE =
            "Já existe uma vacina cadastrada com este nome.";

    public VaccineConflictException() {
        super(MESSAGE);
    }
}
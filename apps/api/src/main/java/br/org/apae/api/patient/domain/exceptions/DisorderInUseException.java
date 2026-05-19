package br.org.apae.api.patient.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DisorderInUseException extends RuntimeException {
    private static final String MESSAGE = "Não é possível excluir este transtorno, pois ele está vinculado a um ou mais pacientes.";

    public DisorderInUseException() {
        super(MESSAGE);
    }
}
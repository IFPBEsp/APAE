package br.org.apae.api.patient.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VaccineInUseException extends RuntimeException {
    private static final String MESSAGE = "Não é possível excluir esta vacina, pois ela está vinculada a um ou mais pacientes.";

    public VaccineInUseException() {
        super(MESSAGE);
    }
}

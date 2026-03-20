package br.org.apae.api.appointment.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProfessionalUnavailableException extends RuntimeException {

    public ProfessionalUnavailableException() {
        super("O profissional não possui disponibilidade para este dia e turno.");
    }
}

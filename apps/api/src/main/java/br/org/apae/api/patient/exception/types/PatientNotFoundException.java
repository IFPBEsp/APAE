package br.org.apae.api.patient.exception.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException() {
        super("Paciente não encontrado");
    }

    public PatientNotFoundException(String message) {
        super(message);
    }
}

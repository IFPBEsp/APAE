package br.org.apae.api.patient.exception.types;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VaccineNotFoundException extends RuntimeException {

    public VaccineNotFoundException(String message) {
        super(message);
    }

    public VaccineNotFoundException(UUID vaccineId) {
        this(String.format("Não foi possível encontrar a vacina com o ID: %d", vaccineId));
    }
}
package br.org.apae.api.patient.domain.exceptions;

import java.time.Year;
import java.util.UUID;
import jakarta.persistence.EntityNotFoundException;

public class RegistryNotFoundException extends EntityNotFoundException {

    public RegistryNotFoundException(UUID id) {
        super("Registro anual com o ID: " + id + " não encontrado.");
    }

    public RegistryNotFoundException(Year year) {
        super("Registro anual para o ano: " + year + " não encontrado.");
    }
}
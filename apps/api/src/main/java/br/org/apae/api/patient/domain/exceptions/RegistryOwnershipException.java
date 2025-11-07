package br.org.apae.api.patient.domain.exceptions;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class RegistryOwnershipException extends RuntimeException {

    public RegistryOwnershipException(UUID patientId, UUID registryId) {
        super("O registro com ID " + registryId + " não pertence ao paciente com ID " + patientId);
    }
}
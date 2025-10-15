package br.org.apae.api.common.dto.vaccine.response;

import java.util.UUID;

public record ResponseVaccineDTO(
        UUID id,
        String name
) {
}
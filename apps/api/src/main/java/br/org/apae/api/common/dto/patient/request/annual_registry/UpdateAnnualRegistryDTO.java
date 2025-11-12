package br.org.apae.api.common.dto.patient.request.annual_registry;

import jakarta.validation.constraints.NotNull;

public record UpdateAnnualRegistryDTO(
    @NotNull(message = "O ano é obrigatório")
    Integer year
){}
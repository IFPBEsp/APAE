package br.org.apae.api.common.dto.patient.request.vaccine;

import jakarta.validation.constraints.*;

public record CreateVaccineDTO(
    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    String name
) {}
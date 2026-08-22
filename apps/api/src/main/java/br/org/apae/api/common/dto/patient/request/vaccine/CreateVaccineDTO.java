package br.org.apae.api.common.dto.patient.request.vaccine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateVaccineDTO(
    @NotBlank(message = "O nome da vacina é obrigatório.") 
    @Size(min = 2, max = 100, message = "O nome da vacina deve ter entre 2 e 100 caracteres.") 
    String name
) {}
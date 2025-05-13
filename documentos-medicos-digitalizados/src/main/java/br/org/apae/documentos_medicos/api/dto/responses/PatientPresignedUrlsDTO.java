package br.org.apae.documentos_medicos.api.dto.responses;

import jakarta.validation.constraints.NotBlank;

public record PatientPresignedUrlsDTO(
    @NotBlank(message = "O nome do arquivo é obrigatório.")
    String fileName,

    @NotBlank(message = "O link é obrigatório.")
    String link
) {}

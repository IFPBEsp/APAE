package br.org.apae.documentos_medicos.api.dto.responses;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record MedicalDocumentResponseDTO(
    @NotBlank(message = "O ID é obrigatório.")
    String patientId,

    @NotBlank(message = "A URL é obrigatória.")
    List<PatientPresignedUrlsDTO> urls
) {}

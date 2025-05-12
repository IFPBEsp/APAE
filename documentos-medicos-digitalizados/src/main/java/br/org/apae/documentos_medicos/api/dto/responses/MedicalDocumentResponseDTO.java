package br.org.apae.documentos_medicos.api.dto.responses;

import java.util.List;

public record MedicalDocumentResponseDTO(
    String patientId,
    Integer year,
    List<String> urls
) {}

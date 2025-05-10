package br.org.apae.documentos_medicos.api.dto.responses;

import java.util.List;

public record MedicalDocumentResponseDTO(
    String fileName,
    Integer year,
    String documentType,
    List<String> urls
) {}

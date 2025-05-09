package br.org.apae.documentos_medicos.api.dto.responses;

public record MedicalDocumentResponseDTO(
    String fileName,
    Integer year,
    String documentType,
    String url
) {}

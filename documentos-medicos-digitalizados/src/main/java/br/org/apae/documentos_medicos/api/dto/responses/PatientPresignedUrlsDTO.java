package br.org.apae.documentos_medicos.api.dto.responses;

public record PatientPresignedUrlsDTO(
    String fileName,
    String link
) {}

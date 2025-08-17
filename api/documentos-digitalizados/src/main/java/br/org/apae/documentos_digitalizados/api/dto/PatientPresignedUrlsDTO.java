package br.org.apae.documentos_digitalizados.api.dto;

public record PatientPresignedUrlsDTO(
  String fileName,
  String link
) {}

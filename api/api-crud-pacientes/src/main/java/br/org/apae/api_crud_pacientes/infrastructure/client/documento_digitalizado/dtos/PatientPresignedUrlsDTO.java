package br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos;

public record PatientPresignedUrlsDTO(
  String fileName,
  String link
) {}

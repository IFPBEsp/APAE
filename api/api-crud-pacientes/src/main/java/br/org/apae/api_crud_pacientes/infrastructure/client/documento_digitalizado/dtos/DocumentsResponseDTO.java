package br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos;

import java.util.List;

public record DocumentsResponseDTO(
  String patientId,
  List<PatientPresignedUrlsDTO> urls
) {}

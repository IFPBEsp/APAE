package br.org.apae.api_crud_pacientes.infrastructure.client.documento_digitalizado.dtos;

public record DocumentObjectRequestDTO(
        String patientId,
        Integer year,
        String documentCategory,
        String documentType) { }

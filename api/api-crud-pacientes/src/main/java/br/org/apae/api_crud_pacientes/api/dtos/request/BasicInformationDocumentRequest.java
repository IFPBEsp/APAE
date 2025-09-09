package br.org.apae.api_crud_pacientes.api.dtos.request;

public record BasicInformationDocumentRequest(
        Integer year,
        String documentCategory,
        String documentType) { }

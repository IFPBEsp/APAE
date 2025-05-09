package br.org.apae.documentos_medicos.api.dto.requests;

import org.springframework.web.multipart.MultipartFile;

public record MedicalDocumentUploadDTO(
    String patientId,
    Integer year,
    String documentType,
    MultipartFile file
) {}

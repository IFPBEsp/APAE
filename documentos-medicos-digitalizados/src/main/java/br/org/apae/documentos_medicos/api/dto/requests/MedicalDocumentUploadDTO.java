package br.org.apae.documentos_medicos.api.dto.requests;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record MedicalDocumentUploadDTO(
    UUID patientId,
    Integer year,
    String documentType,
    MultipartFile file
) {}

package br.org.apae.documentos_medicos.api.dto.responses;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record MedicalDocumentResponseDTO(

    UUID id,
    String fileName,
    String description,
    Integer year,
    String documentType,
    LocalDate uploadDate,
    List<String> urls

) {
    public MedicalDocumentResponseDTO(UUID id, String fileName, String description, Integer year, String documentType, LocalDate uploadDate, List<String> urls) {
        this.id = id;
        this.fileName = fileName;
        this.description = description;
        this.year = year;
        this.documentType = documentType;
        this.uploadDate = uploadDate;
        this.urls = urls;
    }
}

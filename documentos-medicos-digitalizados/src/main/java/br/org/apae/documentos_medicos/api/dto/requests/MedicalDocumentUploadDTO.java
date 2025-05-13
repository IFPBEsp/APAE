package br.org.apae.documentos_medicos.api.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicalDocumentUploadDTO {

    @NotNull(message = "O ID é obrigatório.")
    private String patientId;

    @NotBlank(message = "O ano é obrigatório")
    private Integer year;

    @NotNull(message = "O tipo do documento é obrigatório")
    private String documentType;

    public MedicalDocumentUploadDTO(String patientId, Integer year, String documentType) {
        this.patientId = patientId;
        this.year = year;
        this.documentType = documentType;
    }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
}


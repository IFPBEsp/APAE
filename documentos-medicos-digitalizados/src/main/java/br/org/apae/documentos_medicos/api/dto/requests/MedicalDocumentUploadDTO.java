package br.org.apae.documentos_medicos.api.dto.requests;

public class MedicalDocumentUploadDTO {

    private String patientId;
    private Integer year;
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


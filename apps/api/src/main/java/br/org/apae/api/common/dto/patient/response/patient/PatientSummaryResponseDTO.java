package br.org.apae.api.common.dto.patient.response.patient;

import java.time.LocalDate;
import java.util.UUID;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.patient.domain.model.Patient;

public record PatientSummaryResponseDTO(
    UUID id,
    String fullName,
    String birthplace,
    LocalDate birthDate,
    String contact,
    String birthCertificateNumber,
    String registryOffice,
    String fls,
    String book,
    String rg,
    LocalDate issueDate,
    String issuingAgency,
    String cpf,
    String cns,
    String nis,
    LocalDate registrationDate,
    String allergies,
    boolean isStudent,
    boolean isDeleted,
    AddressResponseDTO address) {
  public PatientSummaryResponseDTO(Patient patient, AddressResponseDTO addressResponseDTO) {
    this(
        patient.getId(),
        patient.getFullName(),
        patient.getBirthplace(),
        patient.getBirthDate(),
        patient.getContact(),
        patient.getBirthCertificateNumber(),
        patient.getRegistryOffice(),
        patient.getFls(),
        patient.getBook(),
        patient.getRg(),
        patient.getIssueDate(),
        patient.getIssuingAgency(),
        patient.getCpf(),
        patient.getCns(),
        patient.getNis(),
        patient.getRegistrationDate(),
        patient.getAllergies(),
        patient.isStudent(),
        patient.isDeleted(),
        addressResponseDTO);
  }
}

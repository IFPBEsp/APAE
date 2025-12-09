package br.org.apae.api.common.dto.patient.response.patient;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Patient;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PatientResponseDTO(
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
                AddressResponseDTO address,
                GuardianResponseDTO guardian,
                List<ParentResponseDTO> parents,
                Set<VaccineResponseDTO> vaccineNames,
                String photoUrl) {
        public PatientResponseDTO(Patient patient, AddressResponseDTO addressResponseDTO,
                        GuardianResponseDTO guardianResponseDTO, List<ParentResponseDTO> parentResponseDTOs,
                        Set<VaccineResponseDTO> vaccineResponseDTOs, String photoUrl) {
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
                                addressResponseDTO,
                                guardianResponseDTO,
                                parentResponseDTOs,
                                vaccineResponseDTOs,
                                photoUrl);
        }
}
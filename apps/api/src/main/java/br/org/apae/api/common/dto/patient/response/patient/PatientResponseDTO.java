package br.org.apae.api.common.dto.patient.response.patient;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Patient;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
                AddressResponseDTO address,
                GuardianResponseDTO guardian,
                List<ParentResponseDTO> parents,
                Set<VaccineResponseDTO> vaccineNames) {

        public PatientResponseDTO(Patient patient) {
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
                                patient.getAddress() != null ? new AddressResponseDTO(patient.getAddress()) : null,
                                patient.getGuardian() != null ? new GuardianResponseDTO(patient.getGuardian()) : null,
                                patient.getParents() != null
                                                ? patient.getParents().stream().map(ParentResponseDTO::new).toList()
                                                : Collections.emptyList(),
                                patient.getVaccines() != null
                                                ? patient.getVaccines().stream().map(VaccineResponseDTO::new)
                                                                .collect(Collectors.toSet())
                                                : Collections.emptySet());
        }
}
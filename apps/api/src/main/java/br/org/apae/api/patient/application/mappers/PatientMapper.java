package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.model.Vaccine;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    private final GuardianMapper guardianMapper;
    private final AddressMapper addressMapper;
    private final VaccineMapper vaccineMapper;

    public PatientMapper(GuardianMapper guardianMapper, AddressMapper addressMapper,
            VaccineMapper vaccineMapper) {
        this.guardianMapper = guardianMapper;
        this.addressMapper = addressMapper;
        this.vaccineMapper = vaccineMapper;
    }

    public Patient toEntity(CreatePatientDTO dto, AddressResponseDTO addressDto, GuardianResponseDTO guardianDto,
            Set<VaccineResponseDTO> vaccineDtos) {
        Address address = addressMapper.toEntityFromResponse(addressDto);
        Guardian guardian = guardianMapper.toEntityFromResponse(guardianDto);
        Set<Vaccine> vaccines = vaccineMapper.toEntitySetFromResponse(vaccineDtos);

        return new Patient(
                dto.fullName(),
                dto.nationality(),
                dto.birthDate(),
                dto.contact(),
                dto.birthCertificateNumber(),
                dto.registryOffice(),
                dto.fls(),
                dto.book(),
                dto.rg(),
                dto.issueDate(),
                dto.issuingAgency(),
                dto.cpf(),
                dto.cns(),
                dto.nis(),
                dto.registrationDate(),
                dto.allergies(),
                dto.isStudent(),
                address,
                guardian,
                vaccines);
    }

    public Patient updateEntityFromDto(Patient patient, UpdatePatientDTO dto) {
        Address address = addressMapper.updateEntityFromDto(patient.getAddress(), dto.address());
        Guardian guardian = guardianMapper.updateEntityFromDto(patient.getGuardian(), dto.guardian());
        Set<Vaccine> vaccines = vaccineMapper.toEntitySet(dto.vaccineNames());

        return new Patient(
                patient.getId(),
                dto.fullName(),
                dto.nationality(),
                dto.birthDate(),
                dto.contact(),
                dto.birthCertificateNumber(),
                dto.registryOffice(),
                dto.fls(),
                dto.book(),
                dto.rg(),
                dto.issueDate(),
                dto.issuingAgency(),
                dto.cpf(),
                dto.cns(),
                dto.nis(),
                dto.registrationDate(),
                dto.allergies(),
                dto.isStudent(),
                address,
                guardian,
                vaccines);
    }

    public PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(patient);
    }
}
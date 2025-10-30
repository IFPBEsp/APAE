package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.mappers.AddressMapper;
import br.org.apae.api.common.dto.patient.create.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.update.UpdatePatientDTO;
import br.org.apae.api.common.model.Address;
import br.org.apae.api.patient.domain.model.Guardian;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    private final ParentMapper parentMapper;
    private final GuardianMapper guardianMapper;
    private final AddressMapper addressMapper;

    public PatientMapper(ParentMapper parentMapper, GuardianMapper guardianMapper, AddressMapper addressMapper) {
        this.parentMapper = parentMapper;
        this.guardianMapper = guardianMapper;
        this.addressMapper = addressMapper;
    }

    public Patient toEntity(CreatePatientDTO dto) {
        if (dto == null) {
            return null;
        }

        Address address = addressMapper.toEntity(dto.address());
        Guardian guardian = guardianMapper.toEntity(dto.guardian());
        Patient patient = new Patient(dto.fullName(), dto.birthDate(), dto.registrationDate(), address, guardian);
        patient.setBirthplace(dto.nationality());
        patient.setContact(dto.contact());
        patient.setBirthCertificateNumber(dto.birthCertificateNumber());
        patient.setRegistryOffice(dto.registryOffice());
        patient.setFls(dto.fls());
        patient.setBook(dto.book());
        patient.setRg(dto.rg());
        patient.setIssueDate(dto.issueDate());
        patient.setIssuingAgency(dto.issuingAgency());
        patient.setCpf(dto.cpf());
        patient.setCns(dto.cns());
        patient.setNis(dto.nis());
        patient.setAllergies(dto.allergies());
        patient.setStudent(dto.isStudent());

        if (dto.parents() != null) {
            dto.parents().forEach(parentDTO -> {
                Parent parent = parentMapper.toEntity(parentDTO, patient);
                patient.addParent(parent);
            });
        }
        return patient;
    }

    public void updateEntityFromDto(Patient patient, UpdatePatientDTO dto) {
        if (dto == null || patient == null) {
            return;
        }

        patient.setFullName(dto.fullName());
        patient.setBirthplace(dto.nationality());
        patient.setBirthDate(dto.birthDate());
        patient.setContact(dto.contact());
        patient.setBirthCertificateNumber(dto.birthCertificateNumber());
        patient.setRegistryOffice(dto.registryOffice());
        patient.setFls(dto.fls());
        patient.setBook(dto.book());
        patient.setRg(dto.rg());
        patient.setIssueDate(dto.issueDate());
        patient.setIssuingAgency(dto.issuingAgency());
        patient.setCpf(dto.cpf());
        patient.setCns(dto.cns());
        patient.setNis(dto.nis());
        patient.setRegistrationDate(dto.registrationDate());
        patient.setAllergies(dto.allergies());
        patient.setStudent(dto.isStudent());

        if (patient.getAddress() != null && dto.address() != null) {
            addressMapper.updateEntityFromDto(patient.getAddress(), dto.address());
        }

        if (patient.getGuardian() != null && dto.guardian() != null) {
            guardianMapper.updateEntityFromDto(patient.getGuardian(), dto.guardian());
        }

        patient.clearParents();
        if (dto.parents() != null) {
            dto.parents().forEach(parentDTO -> {
                Parent parent = parentMapper.toEntity(parentDTO, patient);
                patient.addParent(parent);
            });
        }
    }

    public PatientResponseDTO toResponseDTO(Patient patient) {
        return new PatientResponseDTO(patient);
    }
}
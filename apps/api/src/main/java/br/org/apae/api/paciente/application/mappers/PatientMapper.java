package br.org.apae.api.paciente.application.mappers;

import br.org.apae.api.paciente.domain.model.Address;
import br.org.apae.api.paciente.domain.model.Guardian;
import br.org.apae.api.paciente.domain.model.Parent;
import br.org.apae.api.paciente.domain.model.Patient;
import br.org.apae.api.common.dto.paciente.dto.create.CreateAddressDTO;
import br.org.apae.api.common.dto.paciente.dto.create.CreateGuardianDTO;
import br.org.apae.api.common.dto.paciente.dto.create.CreateParentDTO;
import br.org.apae.api.common.dto.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdateAddressDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdateGuardianDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdateParentDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdatePatientDTO;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PatientMapper {

    public Patient toEntity(CreatePatientDTO dto) {

        Patient patient = new Patient();
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

        patient.setAddress(toAddressEntity(dto.address()));
        patient.setGuardian(toGuardianEntity(dto.guardian()));

        patient.setParents(dto.parents().stream()
                .map(parentDTO -> toParentEntity(parentDTO, patient))
                .collect(Collectors.toList()));

        return patient;
    }

    public void updateEntityFromDTO(Patient patient, UpdatePatientDTO dto) {
        if (dto == null) {
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

        updateAddressEntity(patient.getAddress(), dto.address());
        updateGuardianEntity(patient.getGuardian(), dto.guardian());

        patient.getParents().clear();
        if (dto.parents() != null) {
            dto.parents().forEach(parentDTO -> {
                patient.getParents().add(toParentEntity(parentDTO, patient));
            });
        }
    }

    private Address toAddressEntity(CreateAddressDTO dto) {
        Address address = new Address();
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setCep(dto.cep());
        address.setComplement(dto.complement());
        return address;
    }

    private Guardian toGuardianEntity(CreateGuardianDTO dto) {
        Guardian guardian = new Guardian();
        guardian.setName(dto.name());
        guardian.setContact(dto.contact());
        guardian.setKinship(dto.kinship());
        return guardian;
    }

    private Parent toParentEntity(CreateParentDTO dto, Patient patient) {
        Parent parent = new Parent();
        parent.setName(dto.name());
        parent.setRg(dto.rg());
        parent.setCpf(dto.cpf());
        parent.setProfession(dto.profession());
        parent.setKinship(dto.kinship());
        parent.setPatient(patient);
        return parent;
    }

    private Parent toParentEntity(UpdateParentDTO dto, Patient patient) {
        Parent parent = new Parent();
        parent.setName(dto.name());
        parent.setRg(dto.rg());
        parent.setCpf(dto.cpf());
        parent.setProfession(dto.profession());
        parent.setKinship(dto.kinship());
        parent.setPatient(patient);
        return parent;
    }

    private void updateAddressEntity(Address address, UpdateAddressDTO dto) {
        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setCep(dto.cep());
        address.setComplement(dto.complement());
    }

    private void updateGuardianEntity(Guardian guardian, UpdateGuardianDTO dto) {
        guardian.setName(dto.name());
        guardian.setContact(dto.contact());
        guardian.setKinship(dto.kinship());
    }
}


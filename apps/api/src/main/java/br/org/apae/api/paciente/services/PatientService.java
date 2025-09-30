package br.org.apae.api.paciente.services;

import br.org.apae.api.paciente.domain.model.Address;
import br.org.apae.api.paciente.domain.model.Guardian;
import br.org.apae.api.paciente.domain.model.Parent;
import br.org.apae.api.paciente.domain.model.Patient;
import br.org.apae.api.paciente.domain.repository.PatientRepository;
import br.org.apae.api.paciente.domain.repository.PatientSpecification;
import br.org.apae.api.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.dto.filter.PatientFilterDTO;
import br.org.apae.api.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.dto.update.UpdatePatientDTO;
import br.org.apae.api.paciente.exception.types.PatientNotFoundException;
import br.org.apae.api.paciente.facade.IPatientFacade;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService implements IPatientFacade {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public void createPatient(CreatePatientDTO createPatientDTO) {
        Patient patient = new Patient();

        patient.setFullName(createPatientDTO.fullName());
        patient.setBirthplace(createPatientDTO.nationality());
        patient.setBirthDate(createPatientDTO.birthDate());
        patient.setContact(createPatientDTO.contact());
        patient.setBirthCertificateNumber(createPatientDTO.birthCertificateNumber());
        patient.setRegistryOffice(createPatientDTO.registryOffice());
        patient.setFls(createPatientDTO.fls());
        patient.setBook(createPatientDTO.book());
        patient.setRg(createPatientDTO.rg());
        patient.setIssueDate(createPatientDTO.issueDate());
        patient.setIssuingAgency(createPatientDTO.issuingAgency());
        patient.setCpf(createPatientDTO.cpf());
        patient.setCns(createPatientDTO.cns());
        patient.setNis(createPatientDTO.nis());
        patient.setRegistrationDate(createPatientDTO.registrationDate());
        patient.setAllergies(createPatientDTO.allergies());
        patient.setStudent(createPatientDTO.isStudent());

        Address address = new Address();
        address.setStreet(createPatientDTO.address().street());
        address.setNumber(createPatientDTO.address().number());
        address.setNeighborhood(createPatientDTO.address().neighborhood());
        address.setCity(createPatientDTO.address().city());
        address.setState(createPatientDTO.address().state());
        address.setZipCode(createPatientDTO.address().zipCode());
        address.setComplement(createPatientDTO.address().complement());
        patient.setAddress(address);

        Guardian guardian = new Guardian();
        guardian.setName(createPatientDTO.guardian().name());
        guardian.setContact(createPatientDTO.guardian().contact());
        guardian.setKinship(createPatientDTO.guardian().kinship());
        patient.setGuardian(guardian);

        List<Parent> parents = createPatientDTO.parents().stream().map(parentDTO -> {
            Parent parent = new Parent();
            parent.setName(parentDTO.name());
            parent.setRg(parentDTO.rg());
            parent.setCpf(parentDTO.cpf());
            parent.setProfession(parentDTO.profession());
            parent.setKinship(parentDTO.kinship());
            parent.setPatient(patient);
            return parent;
        }).collect(Collectors.toList());
        patient.setParents(parents);

        patientRepository.save(patient);
    }

    @Override
    public PatientResponseDTO findById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente com ID " + id + " não encontrado."));
        return new PatientResponseDTO(patient);
    }

    @Override
    public List<PatientResponseDTO> findAll() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream()
                .map(PatientResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PatientResponseDTO> findByFilter(Map<String, String> filters) {
        Specification<Patient> spec = PatientSpecification.filterBy(filters);
        return patientRepository.findAll(spec).stream()
                .map(PatientResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        patient.setFullName(updatePatientDTO.fullName());
        patient.setBirthplace(updatePatientDTO.nationality());
        patient.setBirthDate(updatePatientDTO.birthDate());
        patient.setContact(updatePatientDTO.contact());
        patient.setBirthCertificateNumber(updatePatientDTO.birthCertificateNumber());
        patient.setRegistryOffice(updatePatientDTO.registryOffice());
        patient.setFls(updatePatientDTO.fls());
        patient.setBook(updatePatientDTO.book());
        patient.setRg(updatePatientDTO.rg());
        patient.setIssueDate(updatePatientDTO.issueDate());
        patient.setIssuingAgency(updatePatientDTO.issuingAgency());
        patient.setCpf(updatePatientDTO.cpf());
        patient.setCns(updatePatientDTO.cns());
        patient.setNis(updatePatientDTO.nis());
        patient.setRegistrationDate(updatePatientDTO.registrationDate());
        patient.setAllergies(updatePatientDTO.allergies());
        patient.setStudent(updatePatientDTO.isStudent());

        Address address = patient.getAddress();
        address.setCity(updatePatientDTO.address().city());
        address.setZipCode(updatePatientDTO.address().zipCode());
        address.setState(updatePatientDTO.address().state());
        address.setNeighborhood(updatePatientDTO.address().neighborhood());
        address.setStreet(updatePatientDTO.address().street());
        address.setNumber(updatePatientDTO.address().number());
        address.setComplement(updatePatientDTO.address().complement());

        Guardian guardian = patient.getGuardian();
        guardian.setName(updatePatientDTO.guardian().name());
        guardian.setContact(updatePatientDTO.guardian().contact());
        guardian.setKinship(updatePatientDTO.guardian().kinship());

        patient.getParents().clear();
        if (updatePatientDTO.parents() != null) {
            updatePatientDTO.parents().forEach(parentDTO -> {
                Parent parent = new Parent();
                parent.setName(parentDTO.name());
                parent.setRg(parentDTO.rg());
                parent.setCpf(parentDTO.cpf());
                parent.setProfession(parentDTO.profession());
                parent.setKinship(parentDTO.kinship());
                parent.setPatient(patient);
                patient.getParents().add(parent);
            });
        }

        return new PatientResponseDTO(patient);
    }

    @Override
    @Transactional
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException();
        }
        patientRepository.deleteById(id);
    }
}


package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.patient.create.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.update.UpdatePatientDTO;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.application.mappers.PatientMapper;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.PatientSpecification;
import br.org.apae.api.patient.exception.types.PatientNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService implements PatientApplicationService {

    private final PatientRepository patientRepository;
    private final VaccineService vaccineService;
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, VaccineService vaccineService, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.vaccineService = vaccineService;
        this.patientMapper = patientMapper;
    }

    @Override
    @Transactional
    public void createPatient(CreatePatientDTO createPatientDTO) {
        Patient patient = patientMapper.toEntity(createPatientDTO);
        List<Vaccine> vaccines = vaccineService.findAndValidateVaccinesByNames(createPatientDTO.vaccineNames());
        vaccines.forEach(patient::addVaccine);

        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente com ID " + id + " não encontrado."));

        patientMapper.updateEntityFromDto(patient, updatePatientDTO);
        patient.clearVaccines();
        List<Vaccine> vaccines = vaccineService.findAndValidateVaccinesByNames(updatePatientDTO.vaccineNames());
        vaccines.forEach(patient::addVaccine);

        Patient updatedPatient = patientRepository.save(patient);
        return new PatientResponseDTO(updatedPatient);
    }

    @Override
    public PatientResponseDTO findById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente com ID " + id + " não encontrado."));
        return new PatientResponseDTO(patient);
    }

    @Override
    public Page<PatientResponseDTO> findAll(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findAll(pageable);
        return patientsPage.map(PatientResponseDTO::new);
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
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Paciente com ID " + id + " não encontrado para exclusão.");
        }
        patientRepository.deleteById(id);
    }
}
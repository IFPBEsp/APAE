package br.org.apae.api.paciente.application.services;

import br.org.apae.api.paciente.application.facade.IPatientFacade;
import br.org.apae.api.paciente.application.mappers.PatientMapper;
import br.org.apae.api.paciente.domain.model.Patient;
import br.org.apae.api.paciente.domain.repository.PatientRepository;
import br.org.apae.api.paciente.domain.repository.PatientSpecification;
import br.org.apae.api.paciente.exception.types.PatientNotFoundException;
import br.org.apae.api.paciente.interfaces.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.interfaces.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.interfaces.dto.update.UpdatePatientDTO;
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
    private final PatientMapper patientMapper;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

    @Override
    @Transactional
    public void createPatient(CreatePatientDTO createPatientDTO) {
        Patient patient = patientMapper.toEntity(createPatientDTO);
        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(PatientNotFoundException::new);
        patientMapper.updateEntityFromDTO(patient, updatePatientDTO);
        return new PatientResponseDTO(patient);
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
    public void deletePatient(UUID id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException();
        }
        patientRepository.deleteById(id);
    }
}


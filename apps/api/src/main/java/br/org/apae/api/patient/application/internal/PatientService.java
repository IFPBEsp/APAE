package br.org.apae.api.patient.application.internal;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.application.mappers.PatientMapper;
import br.org.apae.api.patient.domain.model.Patient;
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
import java.util.Set;
import java.util.UUID;

@Service
public class PatientService implements PatientApplicationService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final AddressService addressService;
    private final GuardianService guardianService;
    private final VaccineService vaccineService;
    private final ParentService parentService;
    private final AnnualRegistryService annualRegistryService;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper,
            AddressService addressService,
            GuardianService guardianService, VaccineService vaccineService, ParentService parentService,
            AnnualRegistryService annualRegistryService) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.addressService = addressService;
        this.guardianService = guardianService;
        this.vaccineService = vaccineService;
        this.parentService = parentService;
        this.annualRegistryService = annualRegistryService;
    }

    @Override
    @Transactional
    public void createPatient(CreatePatientDTO createPatientDTO) {
        AddressResponseDTO addressDto = this.addressService.createAddress(createPatientDTO.address());
        GuardianResponseDTO guardianDto = this.guardianService.createGuardian(createPatientDTO.guardian());
        Set<VaccineResponseDTO> vaccineDtos = vaccineService.findVaccinesByNames(createPatientDTO.vaccineNames());

        Patient patient = patientMapper.toEntity(createPatientDTO, addressDto, guardianDto, vaccineDtos);

        // TODO: validar se os parentes estão sendo setados para este paciente
        parentService.createParents(createPatientDTO.parents(), patient);
        
        // TODO: validar se o registro anual está sendo setado para este paciente
        annualRegistryService.createRegistry(createPatientDTO.annualRegistry(), patient);
        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Paciente com ID " + id + " não encontrado."));

        Set<VaccineResponseDTO> vaccineDtos = vaccineService.findVaccinesByNames(updatePatientDTO.vaccineNames());

        patientMapper.updateEntityFromDto(patient, updatePatientDTO);

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
                .toList();
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
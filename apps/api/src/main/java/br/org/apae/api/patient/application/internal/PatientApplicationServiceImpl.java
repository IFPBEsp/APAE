package br.org.apae.api.patient.application.internal;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.GuardianApplicationService;
import br.org.apae.api.patient.application.interfaces.ParentApplicationService;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.application.mappers.PatientMapper;
import br.org.apae.api.patient.domain.exceptions.PatientConflictException;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.PatientSpecification;

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
public class PatientApplicationServiceImpl implements PatientApplicationService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientDomainService patientDomainService;

    private final AddressService addressService;
    private final GuardianApplicationService guardianService;
    private final VaccineApplicationService vaccineService;
    private final ParentApplicationService parentService;
    private final AnnualRegistryApplicationService annualRegistryService;
    private final PatientDocumentsService documentService;

    public PatientApplicationServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper,
            PatientDomainService patientDomainService,
            AddressService addressService,
            GuardianApplicationService guardianService, VaccineApplicationService vaccineService,
            ParentApplicationService parentService,
            AnnualRegistryApplicationService annualRegistryService,
            PatientDocumentsService documentService) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientDomainService = patientDomainService;
        this.addressService = addressService;
        this.guardianService = guardianService;
        this.vaccineService = vaccineService;
        this.parentService = parentService;
        this.annualRegistryService = annualRegistryService;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO, CreateDocumentsDTO documents) {
        boolean existingPatient = patientRepository.existsByCpfOrRg(createPatientDTO.cpf(), createPatientDTO.rg());

        if (existingPatient) {
            throw new PatientConflictException();
        }

        AddressResponseDTO addressDto = addressService.createAddress(createPatientDTO.address());
        Set<VaccineResponseDTO> vaccinesDto = vaccineService.findVaccines(createPatientDTO.vaccineNames());

        Patient patient = patientMapper.toEntity(createPatientDTO, addressDto, vaccinesDto);

        patientRepository.save(patient);

        annualRegistryService.createRegistry(createPatientDTO.annualRegistry(), patient.getId());
        GuardianResponseDTO guardianDto = guardianService.createGuardian(createPatientDTO.guardian(), patient.getId());
        List<ParentResponseDTO> parentDtos = parentService.createParents(createPatientDTO.parents(), patient.getId());
        documentService.storePatientDocuments(patient, documents);

        return patientMapper.toResponseDTO(patient, guardianDto, parentDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO findPatientById(UUID id) {
        Patient patient = patientDomainService.getByIdOrThrow(id);

        List<ParentResponseDTO> parentDtos = parentService.findParentsByPatientId(id);
        GuardianResponseDTO guardianDto = guardianService.findGuardianByPatientId(id);

        return patientMapper.toResponseDTO(patient, guardianDto, parentDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientSummaryResponseDTO> findAllPatients(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findAll(pageable);
        return patientsPage.map(patientMapper::toSummaryResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientSummaryResponseDTO> findPatientByFilter(Map<String, String> filters) {
        Specification<Patient> spec = PatientSpecification.filterBy(filters);
        return patientRepository.findAll(spec).stream()
                .map(patientMapper::toSummaryResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientDomainService.getByIdOrThrow(id);

        Set<VaccineResponseDTO> vaccineDtos = vaccineService.findVaccines(updatePatientDTO.vaccineNames());
        AddressResponseDTO addressDto = addressService.updateAddress(patient.getAddress().getId(),
                updatePatientDTO.address());

        Patient updatedPatient = patientMapper.updateEntityFromDto(patient, updatePatientDTO, addressDto, vaccineDtos);

        patientRepository.save(updatedPatient);

        GuardianResponseDTO guardianDto = guardianService.updateGuardian(updatePatientDTO.guardian(), id);
        List<ParentResponseDTO> parentDtos = parentService.updateParents(updatePatientDTO.parents(), id);

        return patientMapper.toResponseDTO(updatedPatient, guardianDto, parentDtos);
    }

    @Override
    @Transactional
    public void disablePatient(UUID id) {
        Patient patient = patientDomainService.getByIdOrThrow(id);
        patient.setDeleted(true);

        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void deletePatient(UUID id) {
        Patient patient = patientDomainService.getByIdOrThrow(id);

        addressService.deleteAddress(patient.getAddress().getId());
        guardianService.deleteGuardian(patient.getId());
        parentService.deleteParents(patient.getId());
        annualRegistryService.deleteAllRegistriesByPatient(patient.getId());

        patientRepository.save(patient);
    }
}
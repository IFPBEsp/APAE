package br.org.apae.api.patient.application.internal;

import br.org.apae.api.appointment.domain.repository.AbsenceRepository;
import br.org.apae.api.appointment.mapper.AbsenceMapper;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientWithAbsencesResponseDTO;
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

import br.org.apae.api.patient.domain.repository.projection.PatientWithAbsenceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientApplicationServiceImpl implements PatientApplicationService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PatientDomainService patientDomainService;

    private final GuardianApplicationService guardianService;
    private final VaccineApplicationService vaccineService;
    private final ParentApplicationService parentService;
    private final AnnualRegistryApplicationService annualRegistryService;
    private final PatientDocumentsService documentService;
    private final AbsenceRepository absenceRepository;
    private final AbsenceMapper absenceMapper;

    public PatientApplicationServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper,
            PatientDomainService patientDomainService,
            GuardianApplicationService guardianService, VaccineApplicationService vaccineService,
            ParentApplicationService parentService,
            AnnualRegistryApplicationService annualRegistryService,
            PatientDocumentsService documentService,
            AbsenceRepository absenceRepository,
                                         AbsenceMapper absenceMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.patientDomainService = patientDomainService;
        this.guardianService = guardianService;
        this.vaccineService = vaccineService;
        this.parentService = parentService;
        this.annualRegistryService = annualRegistryService;
        this.documentService = documentService;
        this.absenceRepository = absenceRepository;
        this.absenceMapper = absenceMapper;
    }

    @Override
    @Transactional
    public PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO, CreateDocumentsDTO documents) {
        boolean existingPatient = patientRepository.existsByCpfOrRg(createPatientDTO.cpf(), createPatientDTO.rg());

        if (existingPatient) {
            throw new PatientConflictException();
        }

        Set<VaccineResponseDTO> vaccinesDto = vaccineService.findVaccines(createPatientDTO.vaccineNames());

        Patient patient = patientMapper.toEntity(createPatientDTO, vaccinesDto);

        patientRepository.save(patient);

        annualRegistryService.createRegistry(createPatientDTO.annualRegistry(), patient.getId());
        GuardianResponseDTO guardianDto = guardianService.createGuardian(createPatientDTO.guardian(), patient.getId());
        List<ParentResponseDTO> parentDtos = parentService.createParents(createPatientDTO.parents(), patient.getId());
        documentService.storePatientDocuments(patient, documents);
        String photo = documentService.getPatientPhoto(patient.getId());

        return patientMapper.toResponseDTO(patient, guardianDto, parentDtos, photo);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientResponseDTO findPatientById(UUID id) {
        Patient patient = patientDomainService.getByIdOrThrow(id);

        List<ParentResponseDTO> parentDtos = parentService.findParentsByPatientId(id);
        GuardianResponseDTO guardianDto = guardianService.findGuardianByPatientId(id);

        String photo = documentService.getPatientPhoto(id);

        return patientMapper.toResponseDTO(patient, guardianDto, parentDtos, photo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientSummaryResponseDTO> findAllPatients(Pageable pageable) {
        Page<Patient> patientsPage = patientRepository.findAll(pageable);
        return patientsPage.map(patient -> {
            String photo = documentService.getPatientPhoto(patient.getId());
            return patientMapper.toSummaryResponseDTO(patient, photo);
        });
    }

   @Override
    @Transactional(readOnly = true)
    public Page<PatientSummaryResponseDTO> findPatientByFilter(Map<String, String> filters, Pageable pageable) {
        Specification<Patient> spec = PatientSpecification.filterBy(filters);

        return patientRepository.findAll(spec, pageable)
                .map(patient -> {
                    String photo = documentService.getPatientPhoto(patient.getId());
                    return patientMapper.toSummaryResponseDTO(patient, photo);
                });
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        Patient patient = patientDomainService.getByIdOrThrow(id);

        Set<VaccineResponseDTO> vaccineDtos = vaccineService.findVaccines(updatePatientDTO.vaccineNames());

        Patient updatedPatient = patientMapper.updateEntityFromDto(patient, updatePatientDTO, vaccineDtos);

        patientRepository.save(updatedPatient);

        GuardianResponseDTO guardianDto = guardianService.updateGuardian(updatePatientDTO.guardian(), id);
        List<ParentResponseDTO> parentDtos = parentService.updateParents(updatePatientDTO.parents(), id);

        String photo = documentService.getPatientPhoto(id);

        return patientMapper.toResponseDTO(updatedPatient, guardianDto, parentDtos, photo);
    }

    @Override
    @Transactional
    public PatientResponseDTO updatePatientPhoto(UUID id, org.springframework.web.multipart.MultipartFile photo) {
        Patient patient = patientDomainService.getByIdOrThrow(id);
        documentService.storePatientPhoto(patient, photo);

        GuardianResponseDTO guardianDto = guardianService.findGuardianByPatientId(id);
        List<ParentResponseDTO> parentDtos = parentService.findParentsByPatientId(id);
        String photoUrl = documentService.getPatientPhoto(id);

        return patientMapper.toResponseDTO(patient, guardianDto, parentDtos, photoUrl);
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

        guardianService.deleteGuardian(patient.getId());
        parentService.deleteParents(patient.getId());
        annualRegistryService.deleteAllRegistriesByPatient(patient.getId());

        patientRepository.save(patient);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientWithAbsencesResponseDTO> findPatientsWithAbsences(
            Integer minAbsences,
            Pageable pageable
    ) {
        Page<PatientWithAbsenceProjection> page =
                patientRepository.findPatientsWithAbsences(minAbsences, pageable);

        List<UUID> patientIds = page.getContent()
                .stream()
                .map(row -> row.getPatient().getId())
                .toList();

        final Map<UUID, List<AbsenceResponseDTO>> absencesGrouped;

        if (!patientIds.isEmpty()) {
            absencesGrouped =
                    absenceRepository.findByPatientIds(patientIds)
                            .stream()
                            .collect(Collectors.groupingBy(
                                    a -> a.getGeneratedAppointment().getPatientId(),
                                    Collectors.mapping(
                                            absenceMapper::toAbsenceResponse,
                                            Collectors.toList()
                                    )
                            ));
        } else {
            absencesGrouped = Map.of();
        }

        return page.map(row -> {
            UUID patientId = row.getPatient().getId();

            String photo = documentService.getPatientPhoto(patientId);

            List<AbsenceResponseDTO> absences =
                    absencesGrouped.getOrDefault(patientId, List.of());

            return new PatientWithAbsencesResponseDTO(
                    patientMapper.toSummaryResponseDTO(row.getPatient(), photo),
                    row.getAbsenceCount(),
                    row.getLastAbsenceDate(),
                    absences
            );
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findAllPatientCities() {
        return patientRepository.findDistinctCities();
    }
}

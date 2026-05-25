package br.org.apae.api.professional.application.internal;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.request.documents.UpdateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.application.mappers.HealthProfessionalMapper;
import br.org.apae.api.professional.domain.exceptions.EmailConflictException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.exceptions.IdentityDocumentConflictException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HealthProfessionalApplicationServiceImpl implements HealthProfessionalApplicationService {

    private final HealthProfessionalRepository repository;
    private final HealthProfessionalMapper mapper;
    private final ProfessionalDocumentsService documentsService;
    private final ServiceAreaApplicationService serviceAreaApplicationService;
    private final DocumentApplicationService documentService;

    public HealthProfessionalApplicationServiceImpl(HealthProfessionalRepository repository,
            HealthProfessionalMapper mapper, ProfessionalDocumentsService documentsService,
            ServiceAreaApplicationService serviceAreaApplicationService, DocumentApplicationService documentService) {
        this.repository = repository;
        this.mapper = mapper;
        this.documentsService = documentsService;
        this.serviceAreaApplicationService = serviceAreaApplicationService;
        this.documentService = documentService;
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO createProfessional(
            CreateHealthProfessionalDTO dto,
            CreateProfessionalDocumentsDTO documentsDTO,
            MultipartFile profilePhoto
    ) {
        if (dto.professionalDocument() != null && repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }
        if (repository.existsByEmail(dto.email())) {
            throw new EmailConflictException();
        }
        if (dto.professionalDocument() != null && repository.existsByIdentityDocument(dto.identityDocument())) {
            throw new IdentityDocumentConflictException();
        }

        ServiceAreaResponseDTO serviceAreaDto = serviceAreaApplicationService
                .findServiceAreaByArea(dto.serviceArea().area());

        HealthProfessional professionalToSave = mapper.toEntity(dto, serviceAreaDto);
        HealthProfessional savedProfessional = repository.save(professionalToSave);

        documentsService.storeProfessionalDocuments(professionalToSave, documentsDTO);

        if (profilePhoto != null && !profilePhoto.isEmpty()) {
            uploadProfessionalPhoto(savedProfessional.getId(), profilePhoto);
        }

        return mapper.toResponseDTO(savedProfessional);
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO updateProfessional(UUID id, UpdateHealthProfessionalDTO dto) {
        HealthProfessional entityToUpdate = repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        if (!entityToUpdate.getEmail().equalsIgnoreCase(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new ProfessionalDocumentConflictException();
        }

        if (dto.professionalDocument() != null && existsByProfessionalDocumentAndIdNot(dto.professionalDocument(), id)){
            throw new ProfessionalDocumentConflictException();
        }

        String area = (dto.serviceArea() != null
                && dto.serviceArea().area() != null
                && !dto.serviceArea().area().isBlank())
                ? dto.serviceArea().area()
                : entityToUpdate.getServiceArea().getArea();

        ServiceAreaResponseDTO serviceAreaDto = serviceAreaApplicationService.findServiceAreaByArea(area);

        HealthProfessional updatedProfessional = mapper.updateEntityFromDto(entityToUpdate, dto, serviceAreaDto);

        repository.save(updatedProfessional);

        return mapper.toResponseDTO(updatedProfessional);
    }

    @Override
    @Transactional(readOnly = true)
    public HealthProfessionalResponseDTO findProfessionalById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(HealthProfessionalNotFoundException::new);
    }

    @Override
    @Transactional
    public void activateProfessional(UUID id) {
        repository.findById(id).orElseThrow(HealthProfessionalNotFoundException::new).setAtivo(true);
    }

    @Override
    @Transactional
    public void inactivateProfessional(UUID id) {
        repository.findById(id).orElseThrow(HealthProfessionalNotFoundException::new).setAtivo(false);
    }

    @Override
    @Transactional
    public void reactivateProfessional(UUID id) {
        repository.findById(id).orElseThrow(HealthProfessionalNotFoundException::new).setAtivo(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HealthProfessionalResponseDTO> findAllProfessionals(Boolean ativo, Pageable pageable) {
        Page<HealthProfessional> page = ativo == null
                ? repository.findAll(pageable)
                : repository.findByAtivo(ativo, pageable);

        return page.map(mapper::toResponseDTO);
    }

    @Override
    @Transactional
    public void updateProfessionalDocuments(UUID id, UpdateProfessionalDocumentsDTO dto) {
        HealthProfessional professional = repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        documentsService.updateProfessionalDocuments(professional, dto);
    }

    @Override
    @Transactional
    public void removeProfessionalDocument(UUID professionalId, UUID documentId) {
        HealthProfessional professional = repository.findById(professionalId)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        documentsService.removeProfessionalDocument(professional, documentId);
    }

    @Override
    @Transactional
    public void uploadProfessionalPhoto(UUID id, MultipartFile file) {

        HealthProfessional professional = repository.findById(id)
            .orElseThrow(HealthProfessionalNotFoundException::new);

        if (file.isEmpty()) {
            throw new RuntimeException("Arquivo vazio");
        }

        String contentType = file.getContentType();

        List<String> allowedTypes = List.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
        );

        if (!allowedTypes.contains(contentType)) {
            throw new RuntimeException("Tipo de arquivo inválido");
        }

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException("Arquivo excede 5MB");
        }

        try {

            String fileName = UUID.randomUUID()
                + "-"
                + file.getOriginalFilename();

            var document = documentService.putDocument(
                new br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO(
                    file.getInputStream(),
                    file.getContentType(),
                    "professional-profile-photo",
                    "profile",
                    professional.getId().toString(),
                    String.valueOf(LocalDate.now().getYear())
                )
            );

            String photoUrl = documentService.getPresignedDocumentUrl(
                new br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO(
                    document.owner(),
                    document.name(),
                    60 * 60 * 24 * 7
                )
            );

            professional.setProfilePhoto(photoUrl);

            repository.save(professional);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar foto");
        }
    }
    
    private List<LocalTime> generateSlots(LocalTime start, LocalTime end) {
        List<LocalTime> slots = new ArrayList<>();

        LocalTime current = start;

        while (current.isBefore(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }

        return slots;
    }

    @Override
    public List<LocalTime> getAvailableTimes(UUID professionalId, LocalDate date) {

        List<LocalTime> occupied = repository.findOccupiedHours(professionalId, date);

        HealthProfessional professional = repository.findById(professionalId)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Day requestedDay = switch (dayOfWeek) {
            case MONDAY    -> Day.SEGUNDA;
            case TUESDAY   -> Day.TERCA;
            case WEDNESDAY -> Day.QUARTA;
            case THURSDAY  -> Day.QUINTA;
            case FRIDAY    -> Day.SEXTA;
            default        -> null;
        };

        if (requestedDay == null) return List.of();

        boolean worksManha = professional.getAvailabilities().stream()
                .anyMatch(a -> a.getDay().equals(requestedDay) && a.getShift().equals(Shift.MANHA));

        boolean worksTarde = professional.getAvailabilities().stream()
                .anyMatch(a -> a.getDay().equals(requestedDay) && a.getShift().equals(Shift.TARDE));

        List<LocalTime> allSlots = new ArrayList<>();

        if (worksManha) {
            allSlots.addAll(generateSlots(LocalTime.of(8, 0), LocalTime.of(12, 0)));
        }

        if (worksTarde) {
            allSlots.addAll(generateSlots(LocalTime.of(13, 0), LocalTime.of(17, 0)));
        }

        if (date.isEqual(LocalDate.now())) {

            LocalTime now = LocalTime.now();

            allSlots = allSlots.stream()
                    .filter(slot -> slot.isAfter(now))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        return allSlots.stream()
                .filter(slot -> !occupied.contains(slot))
                .toList();
    }

    @Override
    public boolean existsByProfessionalDocumentAndIdNot(String document, UUID professionalId) {
        return repository.existsByProfessionalDocumentAndIdNot(document, professionalId);
    }
}
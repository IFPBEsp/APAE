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
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class HealthProfessionalApplicationServiceImpl implements HealthProfessionalApplicationService {

    private final HealthProfessionalRepository repository;
    private final HealthProfessionalMapper mapper;
    private final ProfessionalDocumentsService documentsService;

    private final ServiceAreaApplicationService serviceAreaApplicationService;

    public HealthProfessionalApplicationServiceImpl(HealthProfessionalRepository repository,
            HealthProfessionalMapper mapper, ProfessionalDocumentsService documentsService,
            ServiceAreaApplicationService serviceAreaApplicationService) {
        this.repository = repository;
        this.mapper = mapper;
        this.documentsService = documentsService;
        this.serviceAreaApplicationService = serviceAreaApplicationService;
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO createProfessional(CreateHealthProfessionalDTO dto,
            CreateProfessionalDocumentsDTO documentsDTO) {
        if (repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }
        if (repository.existsByEmail(dto.email())) {
            throw new EmailConflictException();
        }
        if (repository.existsByIdentityDocument(dto.identityDocument())) {
            throw new IdentityDocumentConflictException();
        }

        ServiceAreaResponseDTO serviceAreaDto = serviceAreaApplicationService
                .findServiceAreaByArea(dto.serviceArea().area());

        HealthProfessional professionalToSave = mapper.toEntity(dto, serviceAreaDto);

        HealthProfessional savedProfessional = repository.save(professionalToSave);

        documentsService.storeProfessionalDocuments(professionalToSave, documentsDTO);
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

        if (!entityToUpdate.getProfessionalDocument().equalsIgnoreCase(dto.professionalDocument())
                && repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }

        String area =
                (dto.serviceArea() != null
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

    /*    @Override
    @Transactional
    public void deleteProfessional(UUID id) {
        if (!repository.existsById(id)) {
            throw new HealthProfessionalNotFoundException();
        }
        repository.deleteById(id);
    }
*/

    @Override
    @Transactional
    public void activateProfessional(UUID id) {
        HealthProfessional professional = repository.findById(id)
            .orElseThrow(HealthProfessionalNotFoundException::new);

        professional.setAtivo(true);

    }

    @Override
    @Transactional
    public void inactivateProfessional(UUID id) {
        HealthProfessional professional = repository.findById(id)
            .orElseThrow(HealthProfessionalNotFoundException::new);

        professional.setAtivo(false);

    }

    @Override
    @Transactional
    public void reactivateProfessional(UUID id) {
        HealthProfessional professional = repository.findById(id)
            .orElseThrow(HealthProfessionalNotFoundException::new);

        professional.setAtivo(true);
    }

    /*@Override
    @Transactional(readOnly = true)
    public Page<HealthProfessionalResponseDTO> findAllProfessionals(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }*/

    @Override
    @Transactional(readOnly = true)
    public Page<HealthProfessionalResponseDTO> findAllProfessionals(Boolean ativo, Pageable pageable) {
        Page<HealthProfessional> page;
        if (ativo == null) {
            page = repository.findAll(pageable);
        } else{
            page = repository.findByAtivo(ativo, pageable);
        }
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

    private List<LocalTime> generateSlots(LocalTime start, LocalTime end) {
        List<LocalTime> slots = new ArrayList<>();
        LocalTime current = start;

        while (current.isBefore(end)) {
            slots.add(current);
            current = current.plusMinutes(30);
        }

        return slots;
    }

    public List<LocalTime> getAvailableTimes(UUID professionalId, LocalDate date) {
        List<LocalTime> occupied = repository.findOccupiedHours(professionalId, date);

        List<LocalTime> allSlots = generateSlots(
                LocalTime.of(8, 0),
                LocalTime.of(12, 0)
        );

        return allSlots.stream()
                .filter(slot -> !occupied.contains(slot))
                .toList();
    }
}

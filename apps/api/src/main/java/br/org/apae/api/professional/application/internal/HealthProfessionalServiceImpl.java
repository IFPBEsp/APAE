package br.org.apae.api.professional.application.internal;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalService;
import br.org.apae.api.professional.application.mappers.HealthProfessionalMapper;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class HealthProfessionalServiceImpl implements HealthProfessionalService {

    private final HealthProfessionalRepository repository;
    private final HealthProfessionalMapper mapper;

    public HealthProfessionalServiceImpl(HealthProfessionalRepository repository, HealthProfessionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO create(CreateHealthProfessionalDTO dto) {
        if (repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }
        if (repository.existsByEmail(dto.email())) {
            throw new ProfessionalDocumentConflictException();
        }

        HealthProfessional professionalToSave = mapper.toEntity(dto);
        HealthProfessional savedProfessional = repository.save(professionalToSave);
        return mapper.toResponseDTO(savedProfessional);
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO update(UUID id, UpdateHealthProfessionalDTO dto) {
        HealthProfessional entityToUpdate = repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);
        if (!entityToUpdate.getEmail().equalsIgnoreCase(dto.email()) && repository.existsByEmail(dto.email())) {
            throw new ProfessionalDocumentConflictException();
        }

        if (!entityToUpdate.getProfessionalDocument().equalsIgnoreCase(dto.professionalDocument()) && repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }

        mapper.updateEntityFromDto(entityToUpdate, dto);
        HealthProfessional updatedProfessional = repository.save(entityToUpdate);
        return mapper.toResponseDTO(updatedProfessional);
    }

    @Override
    @Transactional(readOnly = true)
    public HealthProfessionalResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(HealthProfessionalNotFoundException::new);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new HealthProfessionalNotFoundException();
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HealthProfessionalResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }
}
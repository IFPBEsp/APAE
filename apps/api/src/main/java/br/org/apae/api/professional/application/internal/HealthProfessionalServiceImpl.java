package br.org.apae.api.professional.application.internal;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalService;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HealthProfessionalServiceImpl implements HealthProfessionalService {

    private final HealthProfessionalRepository repository;

    public HealthProfessionalServiceImpl(HealthProfessionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public HealthProfessionalResponseDTO save(CreateHealthProfessionalDTO dto) {
        if (this.repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }

        if (this.repository.existsByEmail(dto.email())) {
            throw new ProfessionalDocumentConflictException();
        }

        HealthProfessional entityToSave = HealthProfessional.from(dto);

        HealthProfessional savedEntity = this.repository.save(entityToSave);

        return savedEntity.toResponseDTO();
    }

    @Override
    public Page<HealthProfessionalResponseDTO> findAll(Pageable pageable) {
        Page<HealthProfessional> entityPage = this.repository.findAll(pageable);

        return entityPage.map(HealthProfessional::toResponseDTO);
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    @Override
    public HealthProfessionalResponseDTO findById(UUID id) {
        HealthProfessional entity = this.repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        return entity.toResponseDTO();
    }

    @Override
    public HealthProfessionalResponseDTO update(UUID id, UpdateHealthProfessionalDTO dto) {
        HealthProfessional entityToUpdate = this.repository.findById(id)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        entityToUpdate.updateWith(dto);

        HealthProfessional savedEntity = this.repository.save(entityToUpdate);

        return savedEntity.toResponseDTO();
    }
}
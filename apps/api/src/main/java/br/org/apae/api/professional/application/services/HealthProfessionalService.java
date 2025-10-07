package br.org.apae.api.professional.application.services;

import br.org.apae.api.professional.application.interfaces.IHealthProfessionalService;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import br.org.apae.api.professional.exceptions.BusinessValidationException;
import br.org.apae.api.professional.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HealthProfessionalService implements IHealthProfessionalService {

    private final br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository;

    @Autowired
    public HealthProfessionalService(br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public HealthProfessionalResponseDTO save(HealthProfessionalCreateDTO dto) {
        if (this.repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new BusinessValidationException("Professional document already registered.");
        }

        if (this.repository.existsByEmail(dto.email())) {
            throw new BusinessValidationException("Email already registered.");
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
                .orElseThrow(() -> new EntityNotFoundException("Health professional not found."));

        return entity.toResponseDTO();
    }

    @Override
    public HealthProfessionalResponseDTO update(UUID id, HealthProfessionalUpdateDTO dto) {
        HealthProfessional entityExistente = this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Health professional not found."));

        entityExistente.updateWith(dto);

        HealthProfessional savedEntity = this.repository.save(entityExistente);

        return savedEntity.toResponseDTO();
    }
}
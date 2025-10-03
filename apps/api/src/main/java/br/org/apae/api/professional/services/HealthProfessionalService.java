package br.org.apae.api.professional.services;

import br.org.apae.api.common.model.Address;
import br.org.apae.api.professional.facade.IHealthProfessionalFacade;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class HealthProfessionalService implements IHealthProfessionalFacade { // Implementa a nova interface

    private final br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository;
    private final br.org.apae.api.professional.infra.mapper.HealthProfessionalMapper mapper;

    @Autowired
    public HealthProfessionalService(br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository, br.org.apae.api.professional.infra.mapper.HealthProfessionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public HealthProfessionalResponseDTO save(HealthProfessionalCreateDTO dto) {
        // Uso dos record accessors (dto.professionalDocument() ao invés de dto.getProfessionalDocument())
        if (this.repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new br.org.apae.api.professional.domain.exceptions.BusinessValidationException("Professional document already registered.");
        }

        if (this.repository.existsByEmail(dto.email())) {
            throw new br.org.apae.api.professional.domain.exceptions.BusinessValidationException("Email already registered.");
        }

        br.org.apae.api.professional.domain.model.HealthProfessional domainModel = mapper.toDomain(dto);

        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entityToSave = mapper.toEntity(domainModel);

        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity savedEntity = this.repository.save(entityToSave);

        br.org.apae.api.professional.domain.model.HealthProfessional savedModel = mapper.toModel(savedEntity);

        return mapper.toResponseDTO(savedModel);
    }

    @Override
    public Page<HealthProfessionalResponseDTO> findAll(Pageable pageable) {
        Page<br.org.apae.api.professional.infra.entity.HealthProfessionalEntity> entityPage = this.repository.findAll(pageable);

        return entityPage.map(entity -> {
            br.org.apae.api.professional.domain.model.HealthProfessional model = mapper.toModel(entity);
            return mapper.toResponseDTO(model);
        });
    }

    @Override
    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    @Override
    public HealthProfessionalResponseDTO findById(UUID id) {
        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entity = this.repository.findById(id)
                .orElseThrow(() -> new br.org.apae.api.professional.domain.exceptions.EntityNotFoundException("Health professional not found."));

        br.org.apae.api.professional.domain.model.HealthProfessional model = mapper.toModel(entity);
        return mapper.toResponseDTO(model);
    }

    @Override
    public HealthProfessionalResponseDTO update(UUID id, HealthProfessionalUpdateDTO dto) {
        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entityExistente = this.repository.findById(id)
                .orElseThrow(() -> new br.org.apae.api.professional.domain.exceptions.EntityNotFoundException("Health professional not found."));

        br.org.apae.api.professional.domain.model.HealthProfessional modelExistente = mapper.toModel(entityExistente);

        Optional.ofNullable(dto.healthSector()).ifPresent(modelExistente::setHealthSector);
        Optional.ofNullable(dto.professionalDocument()).ifPresent(modelExistente::setProfessionalDocument);
        Optional.ofNullable(dto.name()).ifPresent(modelExistente::setName);
        Optional.ofNullable(dto.email()).ifPresent(modelExistente::setEmail);
        Optional.ofNullable(dto.phoneNumber()).ifPresent(modelExistente::setPhoneNumber);
        Optional.ofNullable(dto.identityDocument()).ifPresent(modelExistente::setIdentityDocument);

        if (dto.address() != null) {
            Address addressModel = modelExistente.getAddress();
            Optional.ofNullable(dto.address().state()).ifPresent(addressModel::setState);
            Optional.ofNullable(dto.address().city()).ifPresent(addressModel::setCity);
            Optional.ofNullable(dto.address().neighborhood()).ifPresent(addressModel::setNeighborhood);
            Optional.ofNullable(dto.address().street()).ifPresent(addressModel::setStreet);
            Optional.ofNullable(dto.address().number()).ifPresent(addressModel::setNumber);
            Optional.ofNullable(dto.address().cep()).ifPresent(addressModel::setCep);
            Optional.ofNullable(dto.address().complement()).ifPresent(addressModel::setComplement);
        }

        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entityParaSalvar = mapper.toEntity(modelExistente);

        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity savedEntity = this.repository.save(entityParaSalvar);

        br.org.apae.api.professional.domain.model.HealthProfessional savedModel = mapper.toModel(savedEntity);
        return mapper.toResponseDTO(savedModel);
    }
}
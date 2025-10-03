package br.org.apae.api.professional.services;

import br.org.apae.api.common.model.Address;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class HealthProfessionalService {

    private final br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository;
    private final br.org.apae.api.professional.infra.mapper.HealthProfessionalMapper mapper;

    @Autowired
    public HealthProfessionalService(br.org.apae.api.professional.domain.repository.HealthProfessionalRepository repository, br.org.apae.api.professional.infra.mapper.HealthProfessionalMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public br.org.apae.api.professional.dto.HealthProfessionalResponseDTO save(HealthProfessionalCreateDTO dto) {
        if (this.repository.existsByProfessionalDocument(dto.getProfessionalDocument())) {
            throw new br.org.apae.api.professional.domain.exceptions.BusinessValidationException("Professional document already registered.");
        }

        if (this.repository.existsByEmail(dto.getEmail())) {
            throw new br.org.apae.api.professional.domain.exceptions.BusinessValidationException("Email already registered.");
        }

        br.org.apae.api.professional.domain.model.HealthProfessional domainModel = mapper.toDomain(dto);

        return getHealthProfessionalResponseDTO(domainModel);
    }

    public Page<br.org.apae.api.professional.dto.HealthProfessionalResponseDTO> findAll(Pageable pageable) {

        Page<br.org.apae.api.professional.infra.entity.HealthProfessionalEntity> entityPage = this.repository.findAll(pageable);


        return entityPage.map(entity -> {
            br.org.apae.api.professional.domain.model.HealthProfessional model = mapper.toModel(entity);
            return mapper.toResponseDTO(model);
        });
    }

    public void delete(UUID id) {
        this.repository.deleteById(id);
    }

    public br.org.apae.api.professional.dto.HealthProfessionalResponseDTO findById(UUID id) {
        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entity = this.repository.findById(id)
                .orElseThrow();

        br.org.apae.api.professional.domain.model.HealthProfessional model = mapper.toModel(entity);
        return mapper.toResponseDTO(model);
    }

    public br.org.apae.api.professional.dto.HealthProfessionalResponseDTO update(UUID id, br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO dto) {
        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entityExistente = this.repository.findById(id)
                .orElseThrow(() -> new br.org.apae.api.professional.domain.exceptions.EntityNotFoundException("Health professional not found."));

        br.org.apae.api.professional.domain.model.HealthProfessional modelExistente = mapper.toModel(entityExistente);

        Optional.ofNullable(dto.getHealthSector()).ifPresent(modelExistente::setHealthSector);
        Optional.ofNullable(dto.getProfessionalDocument()).ifPresent(modelExistente::setProfessionalDocument);
        Optional.ofNullable(dto.getName()).ifPresent(modelExistente::setName);
        Optional.ofNullable(dto.getEmail()).ifPresent(modelExistente::setEmail);
        Optional.ofNullable(dto.getPhoneNumber()).ifPresent(modelExistente::setPhoneNumber);
        Optional.ofNullable(dto.getIdentityDocument()).ifPresent(modelExistente::setIdentityDocument);

        if (dto.getAddress() != null) {
            Address addressModel = modelExistente.getAddress();
            Optional.ofNullable(dto.getAddress().getState()).ifPresent(addressModel::setState);
            Optional.ofNullable(dto.getAddress().getCity()).ifPresent(addressModel::setCity);
            Optional.ofNullable(dto.getAddress().getNeighborhood()).ifPresent(addressModel::setNeighborhood);
            Optional.ofNullable(dto.getAddress().getStreet()).ifPresent(addressModel::setStreet);
            Optional.ofNullable(dto.getAddress().getNumber()).ifPresent(addressModel::setNumber);
            Optional.ofNullable(dto.getAddress().getCep()).ifPresent(addressModel::setCep);
            Optional.ofNullable(dto.getAddress().getComplement()).ifPresent(addressModel::setComplement);
        }

        return getHealthProfessionalResponseDTO(modelExistente);
    }

    private HealthProfessionalResponseDTO getHealthProfessionalResponseDTO(HealthProfessional modelExistente) {
        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity entityParaSalvar = mapper.toEntity(modelExistente);

        br.org.apae.api.professional.infra.entity.HealthProfessionalEntity savedEntity = this.repository.save(entityParaSalvar);

        HealthProfessional savedModel = mapper.toModel(savedEntity);
        return mapper.toResponseDTO(savedModel);
    }
}
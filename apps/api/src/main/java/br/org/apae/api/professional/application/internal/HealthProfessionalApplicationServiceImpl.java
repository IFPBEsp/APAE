package br.org.apae.api.professional.application.internal;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
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
public class HealthProfessionalApplicationServiceImpl implements HealthProfessionalApplicationService {

    private final HealthProfessionalRepository repository;
    private final HealthProfessionalMapper mapper;

    private final AddressService addressService;

    public HealthProfessionalApplicationServiceImpl(HealthProfessionalRepository repository,
            HealthProfessionalMapper mapper, AddressService addressService) {
        this.repository = repository;
        this.mapper = mapper;
        this.addressService = addressService;
    }

    @Override
    @Transactional
    public HealthProfessionalResponseDTO createProfessional(CreateHealthProfessionalDTO dto) {
        if (repository.existsByProfessionalDocument(dto.professionalDocument())) {
            throw new ProfessionalDocumentConflictException();
        }
        if (repository.existsByEmail(dto.email())) {
            throw new ProfessionalDocumentConflictException();
        }

        AddressResponseDTO addressDto = addressService.createAddress(dto.address());

        HealthProfessional professionalToSave = mapper.toEntity(dto, addressDto);

        HealthProfessional savedProfessional = repository.save(professionalToSave);

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

        AddressResponseDTO addressDto = addressService.updateAddress(entityToUpdate.getAddress().getId(),
                dto.address());

        HealthProfessional updatedProfessional = mapper.updateEntityFromDto(entityToUpdate, dto, addressDto);

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
    public void deleteProfessional(UUID id) {
        if (!repository.existsById(id)) {
            throw new HealthProfessionalNotFoundException();
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HealthProfessionalResponseDTO> findAllProfessionals(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponseDTO);
    }
}
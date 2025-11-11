package br.org.apae.api.professional.application.internal;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.application.mappers.HealthProfessionalMapper;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.exceptions.ProfessionalDocumentConflictException;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.Enum.Day;
import br.org.apae.api.professional.domain.model.Enum.Shift;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class HealthProfessionalApplicationServiceImpl implements HealthProfessionalApplicationService {

    private final HealthProfessionalRepository repository;
    private final HealthProfessionalMapper mapper;
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public HealthProfessionalApplicationServiceImpl(HealthProfessionalRepository repository,
                                                    HealthProfessionalMapper mapper,
                                                    AddressService addressService, AddressMapper addressMapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.addressService = addressService;
        this.addressMapper = addressMapper;
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

        addressService.createAddress(dto.address());

        HealthProfessional professionalToSave = mapper.toEntity(dto);
        professionalToSave.setAddress(addressMapper.toEntity(dto.address()));
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

        AddressResponseDTO addressDto = addressService.updateAddress(
                entityToUpdate.getAddress().getId(), dto.address()
        );

        mapper.updateEntityFromDto(entityToUpdate, dto, addressDto);
        updateAvailabilities(entityToUpdate, dto);

        repository.save(entityToUpdate);

        return mapper.toResponseDTO(entityToUpdate);
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
        return repository.findAll(pageable)
                .map(mapper::toResponseDTO);
    }


    private void updateAvailabilities(HealthProfessional entity, UpdateHealthProfessionalDTO dto) {
        if (dto.availabilities() == null) return;

        entity.getAvailabilities().clear();

        List<Availability> updatedAvailabilities = dto.availabilities().stream()
                .map(a -> {
                    Day day = Day.valueOf(a.day().toUpperCase());
                    Shift shift = Shift.valueOf(a.shift().toUpperCase());
                    return new Availability(shift, day, entity);
                })
                .toList();

        entity.getAvailabilities().addAll(updatedAvailabilities);
    }
}

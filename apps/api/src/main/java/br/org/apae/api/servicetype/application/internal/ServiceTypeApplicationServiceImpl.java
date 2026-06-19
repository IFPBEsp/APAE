package br.org.apae.api.servicetype.application.internal;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceTypeNotFoundException;
import br.org.apae.api.servicetype.application.interfaces.ServiceTypeApplicationService;
import br.org.apae.api.servicetype.application.mappers.ServiceTypeMapper;
import br.org.apae.api.servicetype.domain.model.ServiceType;
import br.org.apae.api.servicetype.domain.repository.ServiceTypeRepository;

@Service
public class ServiceTypeApplicationServiceImpl implements ServiceTypeApplicationService {

    private final ServiceTypeRepository repository;
    private final ServiceTypeMapper mapper;

    public ServiceTypeApplicationServiceImpl(ServiceTypeRepository repository, ServiceTypeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ServiceTypeResponseDTO createServiceType(CreateServiceTypeDTO dto) {
        if (repository.existsByArea(dto.name())) {
            throw new ServiceTypeConflictException();
        }

        ServiceType serviceTypeToSave = mapper.toEntity(dto);
        ServiceType savedServiceType = repository.save(serviceTypeToSave);

        return mapper.toResponseDTO(savedServiceType);
    }

    @Override
    @Transactional
    public ServiceTypeResponseDTO updateServiceType(Integer id, UpdateServiceTypeDTO dto) {
        ServiceType entityToUpdate = repository.findById(id)
                .orElseThrow(ServiceTypeNotFoundException::new);

        if (!entityToUpdate.getArea().equalsIgnoreCase(dto.name()) && repository.existsByArea(dto.name())) {
            throw new ServiceTypeConflictException();
        }

        ServiceType updatedServiceType = mapper.updateEntityFromDto(entityToUpdate, dto);
        repository.save(updatedServiceType);

        return mapper.toResponseDTO(updatedServiceType);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceTypeResponseDTO findServiceTypeById(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(ServiceTypeNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceTypeResponseDTO findServiceTypeByArea(String area) {
        return repository.findByArea(area)
                .map(mapper::toResponseDTO)
                .orElseThrow(ServiceTypeNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Set<ServiceTypeResponseDTO> findServiceTypes(Set<CreateServiceTypeDTO> serviceTypeNames) {
        Set<String> areas = serviceTypeNames.stream()
                .map(CreateServiceTypeDTO::name)
                .collect(Collectors.toSet());

        Set<ServiceType> serviceTypes = repository.findByAreaIn(areas);

        return serviceTypes.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteServiceType(Integer id) {
        if (!repository.existsById(id)) {
            throw new ServiceTypeNotFoundException();
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceTypeResponseDTO> findAllServiceTypes() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }
}


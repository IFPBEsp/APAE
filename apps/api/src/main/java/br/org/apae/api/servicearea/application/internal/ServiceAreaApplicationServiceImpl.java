package br.org.apae.api.servicearea.application.internal;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaConflictException;
import br.org.apae.api.professional.domain.exceptions.ServiceAreaNotFoundException;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;
import br.org.apae.api.servicearea.domain.repository.ServiceAreaRepository;

@Service
public class ServiceAreaApplicationServiceImpl implements ServiceAreaApplicationService {

    private final ServiceAreaRepository repository;
    private final ServiceAreaMapper mapper;

    public ServiceAreaApplicationServiceImpl(ServiceAreaRepository repository, ServiceAreaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ServiceAreaResponseDTO createServiceArea(CreateServiceAreaDTO dto) {
        if (repository.existsByArea(dto.area())) {
            throw new ServiceAreaConflictException();
        }

        ServiceArea serviceAreaToSave = mapper.toEntity(dto);
        ServiceArea savedServiceArea = repository.save(serviceAreaToSave);

        return mapper.toResponseDTO(savedServiceArea);
    }

    @Override
    @Transactional
    public ServiceAreaResponseDTO updateServiceArea(Integer id, UpdateServiceAreaDTO dto) {
        ServiceArea entityToUpdate = repository.findById(id)
                .orElseThrow(ServiceAreaNotFoundException::new);

        if (!entityToUpdate.getArea().equalsIgnoreCase(dto.area()) && repository.existsByArea(dto.area())) {
            throw new ServiceAreaConflictException();
        }

        ServiceArea updatedServiceArea = mapper.updateEntityFromDto(entityToUpdate, dto);
        repository.save(updatedServiceArea);

        return mapper.toResponseDTO(updatedServiceArea);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceAreaResponseDTO findServiceAreaById(Integer id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(ServiceAreaNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceAreaResponseDTO findServiceAreaByArea(String area) {
        return repository.findByArea(area)
                .map(mapper::toResponseDTO)
                .orElseThrow(ServiceAreaNotFoundException::new);
    }

    @Override
    @Transactional
    public void deleteServiceArea(Integer id) {
        if (!repository.existsById(id)) {
            throw new ServiceAreaNotFoundException();
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceAreaResponseDTO> findAllServiceAreas() {
        return repository.findAll().stream().map(mapper::toResponseDTO).toList();
    }
}


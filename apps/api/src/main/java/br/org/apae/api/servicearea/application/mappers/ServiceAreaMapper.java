package br.org.apae.api.servicearea.application.mappers;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.servicearea.domain.model.ServiceArea;
import org.springframework.stereotype.Component;

@Component
public class ServiceAreaMapper {

    public ServiceArea toEntity(CreateServiceAreaDTO dto) {
        return new ServiceArea(dto.area());
    }

    public ServiceArea updateEntityFromDto(ServiceArea entity, UpdateServiceAreaDTO dto) {
        entity.setArea(dto.area());
        return entity;
    }

    public ServiceAreaResponseDTO toResponseDTO(ServiceArea entity) {
        return new ServiceAreaResponseDTO(entity);
    }
}


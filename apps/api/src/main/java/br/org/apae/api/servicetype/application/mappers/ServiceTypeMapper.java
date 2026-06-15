package br.org.apae.api.servicetype.application.mappers;

import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.servicetype.domain.model.ServiceType;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ServiceTypeMapper {

    public ServiceType toEntity(CreateServiceTypeDTO dto) {
        return new ServiceType(dto.area());
    }

    public ServiceType toEntityFromResponse(ServiceTypeResponseDTO serviceTypeResponseDTO) {
        return new ServiceType(serviceTypeResponseDTO.id(), serviceTypeResponseDTO.area());
    }

    public ServiceType updateEntityFromDto(ServiceType entity, UpdateServiceTypeDTO dto) {
        entity.setArea(dto.area());
        return entity;
    }

    public Set<ServiceType> toEntitySetFromResponse(Set<ServiceTypeResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toEntityFromResponse)
                .collect(Collectors.toSet());
    }

    public ServiceTypeResponseDTO toResponseDTO(ServiceType entity) {
        return new ServiceTypeResponseDTO(entity);
    }
}


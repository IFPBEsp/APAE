package br.org.apae.api.common.dto.servicearea.response;

import br.org.apae.api.servicearea.domain.model.ServiceArea;


public record ServiceAreaResponseDTO(
        Integer id,
        String area) {

    public ServiceAreaResponseDTO(ServiceArea entity) {
        this(
                entity.getId(),
                entity.getArea());
    }
}


package br.org.apae.api.common.dto.servicetype.response;

import br.org.apae.api.servicetype.domain.model.ServiceType;


public record ServiceTypeResponseDTO(
        Integer id,
        String name) {

    public ServiceTypeResponseDTO(ServiceType entity) {
        this(
                entity.getId(),
                entity.getArea());
    }
}


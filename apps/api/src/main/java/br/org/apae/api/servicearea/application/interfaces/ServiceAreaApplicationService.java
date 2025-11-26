package br.org.apae.api.servicearea.application.interfaces;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ServiceAreaApplicationService {

    ServiceAreaResponseDTO createServiceArea(CreateServiceAreaDTO dto);

    Page<ServiceAreaResponseDTO> findAllServiceAreas(Pageable pageable);

    void deleteServiceArea(UUID id);

    ServiceAreaResponseDTO findServiceAreaById(UUID id);

    ServiceAreaResponseDTO updateServiceArea(UUID id, UpdateServiceAreaDTO dto);
}


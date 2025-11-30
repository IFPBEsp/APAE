package br.org.apae.api.servicearea.application.interfaces;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;

import java.util.List;

public interface ServiceAreaApplicationService {

    ServiceAreaResponseDTO createServiceArea(CreateServiceAreaDTO dto);

    List<ServiceAreaResponseDTO> findAllServiceAreas();

    ServiceAreaResponseDTO findServiceAreaById(Integer id);
    
    ServiceAreaResponseDTO findServiceAreaByArea(String area);
    
    ServiceAreaResponseDTO updateServiceArea(Integer id, UpdateServiceAreaDTO dto);

    void deleteServiceArea(Integer id);
}


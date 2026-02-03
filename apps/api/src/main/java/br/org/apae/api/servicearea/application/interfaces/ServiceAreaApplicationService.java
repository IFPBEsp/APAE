package br.org.apae.api.servicearea.application.interfaces;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ServiceAreaApplicationService {

    ServiceAreaResponseDTO createServiceArea(CreateServiceAreaDTO dto);

    List<ServiceAreaResponseDTO> findAllServiceAreas();

    ServiceAreaResponseDTO findServiceAreaById(Integer id);
    
    ServiceAreaResponseDTO findServiceAreaByArea(String area);
    
    ServiceAreaResponseDTO updateServiceArea(Integer id, UpdateServiceAreaDTO dto);

    @Transactional(readOnly = true)
    Set<ServiceAreaResponseDTO> findServiceAreas(Set<CreateServiceAreaDTO> servicesNames);

    void deleteServiceArea(Integer id);
}


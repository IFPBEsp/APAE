package br.org.apae.api.servicetype.application.interfaces;

import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface ServiceTypeApplicationService {

    ServiceTypeResponseDTO createServiceType(CreateServiceTypeDTO dto);

    List<ServiceTypeResponseDTO> findAllServiceTypes();

    ServiceTypeResponseDTO findServiceTypeById(Integer id);
    
    ServiceTypeResponseDTO findServiceTypeByArea(String area);
    
    ServiceTypeResponseDTO updateServiceType(Integer id, UpdateServiceTypeDTO dto);

    @Transactional(readOnly = true)
    Set<ServiceTypeResponseDTO> findServiceTypes(Set<CreateServiceTypeDTO> serviceTypeNames);

    void deleteServiceType(Integer id);
}


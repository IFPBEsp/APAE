package br.org.apae.api.controllers.servicetype;

import br.org.apae.api.common.dto.servicetype.request.CreateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.request.UpdateServiceTypeDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.servicetype.application.interfaces.ServiceTypeApplicationService;
import br.org.apae.api.servicetype.interfaces.controllers.ServiceTypeController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ServiceTypeControllerImpl implements ServiceTypeController {
    private final ServiceTypeApplicationService service;

    public ServiceTypeControllerImpl(ServiceTypeApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ServiceTypeResponseDTO> createServiceType(
            @RequestBody @Valid CreateServiceTypeDTO dto) {
        ServiceTypeResponseDTO createdServiceType = this.service.createServiceType(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceType);
    }

    @Override
    public ResponseEntity<List<ServiceTypeResponseDTO>> getAllServiceTypes() {
        return ResponseEntity.ok(this.service.findAllServiceTypes());
    }

    @Override
    public ResponseEntity<Void> deleteServiceType(Integer id) {
        this.service.deleteServiceType(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ServiceTypeResponseDTO> findByIdServiceType(Integer id) {
        ServiceTypeResponseDTO dto = service.findServiceTypeById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ServiceTypeResponseDTO> updateServiceType(
            Integer id,
            @RequestBody @Valid UpdateServiceTypeDTO dto) {
        return ResponseEntity.ok(this.service.updateServiceType(id, dto));
    }
}


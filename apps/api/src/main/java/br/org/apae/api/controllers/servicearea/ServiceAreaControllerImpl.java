package br.org.apae.api.controllers.servicearea;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.servicearea.interfaces.controllers.ServiceAreaController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ServiceAreaControllerImpl implements ServiceAreaController {
    private final ServiceAreaApplicationService service;

    public ServiceAreaControllerImpl(ServiceAreaApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<ServiceAreaResponseDTO> createServiceArea(
            @RequestBody @Valid CreateServiceAreaDTO dto) {
        ServiceAreaResponseDTO createdServiceArea = this.service.createServiceArea(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceArea);
    }

    @Override
    public ResponseEntity<Page<ServiceAreaResponseDTO>> getAllServiceAreas(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAllServiceAreas(pageable));
    }

    @Override
    public ResponseEntity<Void> deleteServiceArea(UUID id) {
        this.service.deleteServiceArea(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ServiceAreaResponseDTO> findByIdServiceArea(UUID id) {
        ServiceAreaResponseDTO dto = service.findServiceAreaById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ServiceAreaResponseDTO> updateServiceArea(
            UUID id,
            @RequestBody @Valid UpdateServiceAreaDTO dto) {
        return ResponseEntity.ok(this.service.updateServiceArea(id, dto));
    }
}


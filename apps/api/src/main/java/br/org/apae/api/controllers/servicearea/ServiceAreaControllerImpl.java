package br.org.apae.api.controllers.servicearea;

import br.org.apae.api.common.dto.servicearea.request.CreateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.request.UpdateServiceAreaDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.servicearea.application.interfaces.ServiceAreaApplicationService;
import br.org.apae.api.servicearea.interfaces.controllers.ServiceAreaController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<ServiceAreaResponseDTO>> getAllServiceAreas() {
        return ResponseEntity.ok(this.service.findAllServiceAreas());
    }

    @Override
    public ResponseEntity<Void> deleteServiceArea(Integer id) {
        this.service.deleteServiceArea(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ServiceAreaResponseDTO> findByIdServiceArea(Integer id) {
        ServiceAreaResponseDTO dto = service.findServiceAreaById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<ServiceAreaResponseDTO> updateServiceArea(
            Integer id,
            @RequestBody @Valid UpdateServiceAreaDTO dto) {
        return ResponseEntity.ok(this.service.updateServiceArea(id, dto));
    }
}


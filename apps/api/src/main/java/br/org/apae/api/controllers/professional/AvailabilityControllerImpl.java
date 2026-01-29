package br.org.apae.api.controllers.professional;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.professional.application.interfaces.AvailabilityApplicationService;
import br.org.apae.api.professional.interfaces.controllers.AvailabilityController;
import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;

@RestController
public class AvailabilityControllerImpl implements AvailabilityController {

    private final AvailabilityApplicationService service;

    public AvailabilityControllerImpl(AvailabilityApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AvailabilityResponseDTO> create(
            UUID professionalId,
            CreateAvailabilityDTO dto) {

        AvailabilityResponseDTO response =
                service.createAvailability(professionalId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<List<AvailabilityResponseDTO>> findAll(UUID professionalId) {
        return ResponseEntity.ok(
                service.findAllByProfessional(professionalId)
        );
    }

    @Override
    public ResponseEntity<AvailabilityResponseDTO> update(
            UUID professionalId,
            UUID availabilityId,
            UpdateAvailabilityDTO dto) {

        return ResponseEntity.ok(
                service.updateAvailability(professionalId, availabilityId, dto)
        );
    }

    @Override
    public ResponseEntity<Void> delete(
            UUID professionalId,
            UUID availabilityId) {

        service.deleteAvailability(professionalId, availabilityId);
        return ResponseEntity.noContent().build();
    }
}

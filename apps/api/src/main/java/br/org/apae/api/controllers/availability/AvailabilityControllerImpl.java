package br.org.apae.api.controllers.availability;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.professional.application.interfaces.AvailabilityApplicationService;
import br.org.apae.api.professional.interfaces.controllers.AvailabilityController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AvailabilityControllerImpl implements AvailabilityController {

    private final AvailabilityApplicationService service;

    public AvailabilityControllerImpl(AvailabilityApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AvailabilityResponseDTO> createAvailability(UUID professionalId, CreateAvailabilityDTO dto) {
        AvailabilityResponseDTO created = service.createAvailability(professionalId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<List<AvailabilityResponseDTO>> getAllAvailabilities() {
        List<AvailabilityResponseDTO> availabilities = service.findAllAvailabilities();
        return ResponseEntity.ok(availabilities);
    }

    @Override
    public ResponseEntity<AvailabilityResponseDTO> getAvailabilityById(UUID id) {
        AvailabilityResponseDTO availability = service.findAvailabilityById(id);
        return ResponseEntity.ok(availability);
    }

    @Override
    public ResponseEntity<List<AvailabilityResponseDTO>> getAvailabilitiesByProfessional(UUID professionalId) {
        List<AvailabilityResponseDTO> availabilities = service.findAvailabilitiesByProfessional(professionalId);
        return ResponseEntity.ok(availabilities);
    }

    @Override
    public ResponseEntity<AvailabilityResponseDTO> updateAvailability(UUID id, UpdateAvailabilityDTO dto) {
        AvailabilityResponseDTO updated = service.updateAvailability(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteAvailability(UUID id) {
        service.deleteAvailability(id);
        return ResponseEntity.noContent().build();
    }
}

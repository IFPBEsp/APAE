package br.org.apae.api.professional.interfaces.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import jakarta.validation.Valid;

@Validated
@RequestMapping("/professionals/{professionalId}/availabilities")
public interface AvailabilityController {

    @PostMapping
    ResponseEntity<AvailabilityResponseDTO> create(
            @PathVariable UUID professionalId,
            @Valid @RequestBody CreateAvailabilityDTO dto
    );

    @GetMapping
    ResponseEntity<List<AvailabilityResponseDTO>> findAll(
            @PathVariable UUID professionalId
    );

    @PutMapping("/{availabilityId}")
    ResponseEntity<AvailabilityResponseDTO> update(
            @PathVariable UUID professionalId,
            @PathVariable UUID availabilityId,
            @Valid @RequestBody UpdateAvailabilityDTO dto
    );

    @DeleteMapping("/{availabilityId}")
    ResponseEntity<Void> delete(
            @PathVariable UUID professionalId,
            @PathVariable UUID availabilityId
    );
}

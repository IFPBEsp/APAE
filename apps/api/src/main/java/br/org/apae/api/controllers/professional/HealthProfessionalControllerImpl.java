package br.org.apae.api.controllers.professional;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalService;
import br.org.apae.api.professional.interfaces.controllers.HealthProfessionalController;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class HealthProfessionalControllerImpl implements HealthProfessionalController {
    private final HealthProfessionalService service;

    public HealthProfessionalControllerImpl(HealthProfessionalService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Void> createHealthProfessional(
            @RequestBody @Valid CreateHealthProfessionalDTO dto) {
        this.service.create(dto);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<Page<HealthProfessionalResponseDTO>> getAllHealthProfessional(Pageable pageable) {
        return ResponseEntity.ok(this.service.findAll(pageable));
    }

    @Override
    public ResponseEntity<Void> deleteHealthProfessional(UUID id) {
        this.service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> findByIdHealthProfessional(UUID id) {
        HealthProfessionalResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> updateHealthProfessional(
            UUID id,
            @RequestBody @Valid UpdateHealthProfessionalDTO dto) {
        return ResponseEntity.ok(this.service.update(id, dto));
    }
}

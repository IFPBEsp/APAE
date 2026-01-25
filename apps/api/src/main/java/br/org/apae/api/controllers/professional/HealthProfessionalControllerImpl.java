package br.org.apae.api.controllers.professional;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.documents.CreateProfessionalDocumentsDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.application.interfaces.HealthProfessionalApplicationService;
import br.org.apae.api.professional.interfaces.controllers.HealthProfessionalController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HealthProfessionalControllerImpl implements HealthProfessionalController {
    private final HealthProfessionalApplicationService service;

    public HealthProfessionalControllerImpl(HealthProfessionalApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> createHealthProfessional(CreateHealthProfessionalDTO dto, CreateProfessionalDocumentsDTO documentsDTO) {
        HealthProfessionalResponseDTO createdProfessional = this.service.createProfessional(dto, documentsDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfessional);
    }

    @Override
    public ResponseEntity<Page<HealthProfessionalResponseDTO>> getAllHealthProfessional(Boolean ativo, Pageable pageable) {
        return ResponseEntity.ok(this.service.findAllProfessionals(ativo, pageable));
    }

    /*@Override
    public ResponseEntity<Void> deleteHealthProfessional(UUID id) {
        this.service.deleteProfessional(id);
        return ResponseEntity.noContent().build();
    }*/

    @Override
    public ResponseEntity<Void> inactivateHealthProfessional(UUID id) {
        service.inactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> activateHealthProfessional(UUID id) {
        service.activateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> reactivateHealthProfessional(UUID id) {
        service.reactivateProfessional(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> findByIdHealthProfessional(UUID id) {
        HealthProfessionalResponseDTO dto = service.findProfessionalById(id);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<HealthProfessionalResponseDTO> updateHealthProfessional(
            UUID id, UpdateHealthProfessionalDTO dto) {
        return ResponseEntity.ok(this.service.updateProfessional(id, dto));
    }
}

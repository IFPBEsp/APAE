package br.org.apae.api.controllers.disorder;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.interfaces.controllers.DisorderController;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class DisorderControllerImpl implements DisorderController {

    private final DisorderApplicationService service;

    public DisorderControllerImpl(DisorderApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> createDisorder(@Valid CreateDisorderDTO dto) {
        DisorderResponseDTO createdDisorder = service.createDisorder(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDisorder);
    }

    @Override
    public ResponseEntity<List<DisorderResponseDTO>> getAllDisorders() {
        return ResponseEntity.ok(service.findAllDisorders());
    }

    @Override
    public ResponseEntity<Void> deleteDisorder(UUID id) {
        service.deleteDisorder(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> findByIdDisorder(UUID id) {
        return ResponseEntity.ok(service.findDisorderById(id));
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> updateDisorder(UUID id, @Valid UpdateDisorderDTO dto) {
        DisorderResponseDTO updatedDisorder = service.updateDisorder(id, dto);
        return ResponseEntity.ok(updatedDisorder);
    }
}
package br.org.apae.api.controllers.disorder;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderService;
import br.org.apae.api.patient.interfaces.controllers.DisorderController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
public class DisorderControllerImpl implements DisorderController {

    private final DisorderService service;

    public DisorderControllerImpl(DisorderService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> createDisorder(@Valid CreateDisorderDTO dto) {
        DisorderResponseDTO createdDisorder = service.save(dto);

        URI location = UriComponentsBuilder.fromPath("/transtornos/{id}")
                .buildAndExpand(createdDisorder.id())
                .toUri();

        return ResponseEntity.created(location).body(createdDisorder);
    }

    @Override
    public ResponseEntity<List<DisorderResponseDTO>> getAllDisorders() {
        return ResponseEntity.ok(service.findAll());
    }

    @Override
    public ResponseEntity<Void> deleteDisorder(UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> findByIdDisorder(UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Override
    public ResponseEntity<DisorderResponseDTO> updateDisorder(UUID id, @Valid UpdateDisorderDTO dto) {
        DisorderResponseDTO updatedDisorder = service.update(id, dto);
        return ResponseEntity.ok(updatedDisorder);
    }
}
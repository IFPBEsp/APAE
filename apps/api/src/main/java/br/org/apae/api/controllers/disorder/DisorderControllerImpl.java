package br.org.apae.api.controllers.disorder;

import br.org.apae.api.disorder.application.interfaces.DisorderService;
import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transtornos")
public class DisorderControllerImpl {

    private final DisorderService service;

    public DisorderControllerImpl(DisorderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DisorderResponseDTO> create(@RequestBody @Valid CreateDisorderDTO dto, UriComponentsBuilder uriBuilder) {
        DisorderResponseDTO createdDisorder = service.save(dto);

        URI location = uriBuilder.path("/api/v1/transtornos/{id}")
                .buildAndExpand(createdDisorder.id())
                .toUri();

        return ResponseEntity.created(location).body(createdDisorder);
    }

    @GetMapping
    public ResponseEntity<Page<DisorderResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DisorderResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DisorderResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid UpdateDisorderDTO dto) {
        // Se precisar de validação de campos obrigatórios, o DTO de update deve ser ajustado
        DisorderResponseDTO updatedDisorder = service.update(id, dto);
        return ResponseEntity.ok(updatedDisorder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}

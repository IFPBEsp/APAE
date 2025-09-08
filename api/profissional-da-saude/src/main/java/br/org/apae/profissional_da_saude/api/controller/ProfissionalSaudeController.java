package br.org.apae.profissional_da_saude.api.controller;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeUpdateDTO;
import br.org.apae.profissional_da_saude.application.service.ProfissionalSaudeService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profissionais")
@CrossOrigin(origins = "http://localhost:3000")
public class ProfissionalSaudeController {

  private final ProfissionalSaudeService service;

  @Autowired
  public ProfissionalSaudeController(ProfissionalSaudeService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ProfissionalSaudeResponseDTO> create(@RequestBody @Valid ProfissionalSaudeCreateDTO dto) {
    return ResponseEntity.ok(this.service.save(dto));
  }

  @GetMapping
  public ResponseEntity<Page<ProfissionalSaudeResponseDTO>> getAll(Pageable pageable) {
    return ResponseEntity.ok(this.service.findAll(pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    this.service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfissionalSaudeResponseDTO> findById(@PathVariable UUID id) {
    return service.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProfissionalSaudeResponseDTO> update(
      @PathVariable UUID id,
      @RequestBody @Valid ProfissionalSaudeUpdateDTO dto) {
    return ResponseEntity.ok(this.service.update(id, dto));
  }

  @GetMapping("/areas")
  public ResponseEntity<Page<String>> getAllAreas(Pageable pageable) {
    return ResponseEntity.ok(service.findAllAreas(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfissionalSaudeResponseDTO> getAll(@PathVariable UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }
}

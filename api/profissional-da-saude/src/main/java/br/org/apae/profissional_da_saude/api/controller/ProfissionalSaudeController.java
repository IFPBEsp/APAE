package br.org.apae.profissional_da_saude.api.controller;

import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.ProfissionalSaudeResponseDTO;
import br.org.apae.profissional_da_saude.application.service.ProfissionalSaudeService;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalSaudeController {

  private final ProfissionalSaudeService service;

  @Autowired
  public ProfissionalSaudeController(ProfissionalSaudeService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<ProfissionalSaudeResponseDTO> create(@RequestBody @Valid ProfissionalSaudeCreateDTO dto) {
    return ResponseEntity.ok(service.save(dto));
  }

  @GetMapping
  public ResponseEntity<Page<ProfissionalSaudeResponseDTO>> getAll(Pageable pageable) {
    return ResponseEntity.ok(service.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProfissionalSaudeResponseDTO> getAll(@PathVariable UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }
}

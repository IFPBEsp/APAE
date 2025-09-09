package br.org.apae.profissional_da_saude.api.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.profissional_da_saude.api.dto.AgendamentoCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.AgendamentoUpdateDTO;
import br.org.apae.profissional_da_saude.application.service.AgendamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {
    
  private final AgendamentoService service;

  @Autowired
  public AgendamentoController(AgendamentoService service) {
    this.service = service;
  }
             
  @PostMapping
  public ResponseEntity<AgendamentoResponseDTO> create(@RequestBody @Valid AgendamentoCreateDTO dto) {
    return ResponseEntity.ok(service.create(dto));
  }

  @GetMapping
  public ResponseEntity<Page<AgendamentoResponseDTO>> getAll(
          @RequestParam(required = false) LocalDate data,
          @RequestParam(required = false) LocalTime hora,
          Pageable pageable
  ) {
    if (data != null && hora == null) {
        return ResponseEntity.ok(service.findByProximaConsulta(data, pageable));
    } else if (data != null) {
      return  ResponseEntity.ok(service.findAllByProximaConsultaAndHoraProximaConsulta(data, hora, pageable));
    }
    return ResponseEntity.ok(service.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<AgendamentoResponseDTO> get(@PathVariable UUID id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<AgendamentoResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid AgendamentoUpdateDTO dto){
    return ResponseEntity.ok(service.update(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id){
    service.remove(id);
    return ResponseEntity.noContent().build();
  }
}

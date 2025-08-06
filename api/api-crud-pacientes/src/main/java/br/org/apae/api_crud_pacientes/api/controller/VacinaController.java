package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.request.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.VacinaResponse;
import br.org.apae.api_crud_pacientes.application.service.VacinaService;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {

  private final VacinaService vacinaService;

  public VacinaController(VacinaService vacinaService) {
    this.vacinaService = vacinaService;
  }

  @PostMapping
  public ResponseEntity<VacinaResponse> create(
      @RequestBody VacinaRequest request, UriComponentsBuilder uriBuilder) {
    VacinaResponse response = vacinaService.create(request);
    URI uri = uriBuilder.path("/vacinas/{id}").buildAndExpand(response.getId()).toUri();
    return ResponseEntity.created(uri).body(response);
  }

  @GetMapping
  public ResponseEntity<List<VacinaResponse>> getAll() {
    List<VacinaResponse> vacinas = vacinaService.getAll();
    return ResponseEntity.ok(vacinas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<VacinaResponse> getById(@PathVariable UUID id) {
    VacinaResponse response = vacinaService.getById(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<VacinaResponse> update(
      @PathVariable UUID id, @RequestBody VacinaRequest request) {
    VacinaResponse response = vacinaService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    vacinaService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

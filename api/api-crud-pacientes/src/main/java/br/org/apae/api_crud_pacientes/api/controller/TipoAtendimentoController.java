package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.request.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.TipoAtendimentoResponse;
import br.org.apae.api_crud_pacientes.application.service.TipoAtendimentoService;

import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tipo_atendimento")
public class TipoAtendimentoController {
  private final TipoAtendimentoService tipoAtendimentoService;

  public TipoAtendimentoController(TipoAtendimentoService tipoAtendimentoService) {
    this.tipoAtendimentoService = tipoAtendimentoService;
  }

  @PostMapping
  public ResponseEntity<TipoAtendimentoResponse> create(
      @RequestBody TipoAtendimentoRequest request, UriComponentsBuilder uriBuilder) {

    TipoAtendimentoResponse response = tipoAtendimentoService.create(request);
    URI uri = uriBuilder.path("/tipo_atendimento/{id}").buildAndExpand(response.getId()).toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TipoAtendimentoResponse> update(
      @PathVariable UUID id, @RequestBody TipoAtendimentoRequest request) {
    TipoAtendimentoResponse response = tipoAtendimentoService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<TipoAtendimentoResponse>> getAll(
      @PageableDefault(size = 10) Pageable pageable,
      @RequestParam(required = false) String descricao) {

    Page<TipoAtendimentoResponse> tipoAtendimentos =
        tipoAtendimentoService.getAll(pageable, descricao);
    return ResponseEntity.ok(tipoAtendimentos);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TipoAtendimentoResponse> getById(@PathVariable UUID id) {
    TipoAtendimentoResponse response = tipoAtendimentoService.getById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    tipoAtendimentoService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

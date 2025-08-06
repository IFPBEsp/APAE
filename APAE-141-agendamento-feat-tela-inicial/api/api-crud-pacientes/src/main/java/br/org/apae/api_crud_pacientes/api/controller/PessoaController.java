package br.org.apae.api_crud_pacientes.api.controller;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponse;
import br.org.apae.api_crud_pacientes.application.service.PessoaService;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.PessoaMapper;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
  private final PessoaService pessoaService;
  private final PessoaMapper pessoaMapper;

  public PessoaController(PessoaService pessoaService, PessoaMapper pessoaMapper) {
    this.pessoaService = pessoaService;
    this.pessoaMapper = pessoaMapper;
  }

  @PostMapping
  public ResponseEntity<PessoaResponse> create(
      @RequestBody PessoaRequest request, UriComponentsBuilder uriBuilder) {

    PessoaEntity pessoa = pessoaService.create(request);
    URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(pessoa.getId()).toUri();

    PessoaResponse response = pessoaMapper.toResponse(pessoaService.getById(pessoa.getId()));
    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PessoaResponse> update(
      @PathVariable UUID id, @RequestBody PessoaRequest request) {
    PessoaResponse response = pessoaService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<PessoaResponse>> getAll(
      @PageableDefault(size = 10) Pageable pageable) {
    Page<PessoaResponse> pessoas = pessoaService.getAll(pageable);
    return ResponseEntity.ok(pessoas);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PessoaResponse> getById(@PathVariable UUID id) {
    PessoaResponse response = pessoaMapper.toResponse(pessoaService.getById(id));
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    pessoaService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

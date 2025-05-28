package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.application.service.PessoaResponsavelService;

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
@RequestMapping("/pessoa_responsavel")
public class PessoaResponsavelController {
  private final PessoaResponsavelService pessoaResponsavelService;

  public PessoaResponsavelController(PessoaResponsavelService pessoaResponsavelService) {
    this.pessoaResponsavelService = pessoaResponsavelService;
  }

  @PostMapping
  public ResponseEntity<PessoaResponsavelResponse> create(
      @RequestBody PessoaResponsavelRequest request, UriComponentsBuilder uriBuilder) {

    PessoaResponsavelResponse response = pessoaResponsavelService.create(request);
    URI uri = uriBuilder.path("/pessoa_responsavel/{id}").buildAndExpand(response.getId()).toUri();

    return ResponseEntity.created(uri).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PessoaResponsavelResponse> update(
      @PathVariable UUID id, @RequestBody PessoaResponsavelRequest request) {
    PessoaResponsavelResponse response = pessoaResponsavelService.update(id, request);
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Page<PessoaResponsavelResponse>> getAll(
      @PageableDefault(size = 10) Pageable pageable, @RequestParam(required = false) String nome) {

    Page<PessoaResponsavelResponse> pessoaResponsaveis =
        pessoaResponsavelService.getAll(pageable, nome);
    return ResponseEntity.ok(pessoaResponsaveis);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PessoaResponsavelResponse> getById(@PathVariable UUID id) {
    PessoaResponsavelResponse response = pessoaResponsavelService.getById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    pessoaResponsavelService.delete(id);
    return ResponseEntity.noContent().build();
  }
}

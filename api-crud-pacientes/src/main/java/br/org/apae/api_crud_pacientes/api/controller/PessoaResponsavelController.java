package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.domain.service.PessoaResponsavelService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pessoa_responsavel")
public class PessoaResponsavelController {
    private final PessoaResponsavelService pessoaResponsavelService;

    public PessoaResponsavelController(PessoaResponsavelService pessoaResponsavelService) {
        this.pessoaResponsavelService = pessoaResponsavelService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponsavelResponse> create(
            @RequestBody PessoaResponsavelRequest request,
            @RequestParam UUID pessoaId,
            UriComponentsBuilder uriBuilder) {

        PessoaResponsavelResponse response = pessoaResponsavelService.create(request, pessoaId);
        URI uri = uriBuilder.path("/pessoa_responsavel/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponsavelResponse> update(@PathVariable UUID id,
                                                             @RequestBody PessoaResponsavelRequest request) {
        PessoaResponsavelResponse response = pessoaResponsavelService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PessoaResponsavelResponse>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String nome) {

        Page<PessoaResponsavelResponse> pessoaResponsaveis = pessoaResponsavelService.getAll(pageable, nome);
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

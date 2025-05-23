package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.contato.ContatoResponse;
import br.org.apae.api_crud_pacientes.domain.service.ContatoService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/contatos")
public class ContatoController {
    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping
    public ResponseEntity<ContatoResponse> create(
            @RequestBody ContatoRequest request, // Parâmetro do ID pessoa
            UriComponentsBuilder uriBuilder) {

        ContatoResponse response = contatoService.create(request);
        URI uri = uriBuilder.path("/contatos/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> update(@PathVariable UUID id,
            @RequestBody ContatoRequest request) {
        ContatoResponse response = contatoService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ContatoResponse>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String endereco) {

        Page<ContatoResponse> contatos = contatoService.getAll(pageable, endereco);
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> getById(@PathVariable UUID id) {
        ContatoResponse response = contatoService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        contatoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

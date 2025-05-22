package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.domain.service.CadastroAnualService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cadastros-anual")
public class CadastroAnualController {

    private final CadastroAnualService service;

    public CadastroAnualController(CadastroAnualService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CadastroAnualResponse> create(
            @Valid @RequestBody CadastroAnualRequest dto,
            @RequestParam UUID pessoaId,
            UriComponentsBuilder uriBuilder) {

        CadastroAnualResponse response = service.create(dto, pessoaId);
        URI uri = uriBuilder.path("/api/cadastros-anual/{id}").buildAndExpand(response.getId()).toUri();
        CadastroAnualResponse cadastroAnualResponse = service.getById(response.getId());
        return ResponseEntity.created(uri).body(cadastroAnualResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CadastroAnualResponse> getById(@PathVariable UUID id) {
        CadastroAnualResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CadastroAnualResponse>> getAll() {
        List<CadastroAnualResponse> responses = service.getAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CadastroAnualResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody CadastroAnualRequest dto) {
        CadastroAnualResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
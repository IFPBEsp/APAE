package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaResponse;
import br.org.apae.api_crud_pacientes.domain.service.VacinaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vacinas")
public class VacinaController {

    private final VacinaService vacinaService;

    public VacinaController(VacinaService vacinaService) {
        this.vacinaService = vacinaService;
    }

    @PostMapping
    public ResponseEntity<VacinaResponse> create(@RequestBody VacinaRequest request,
    @RequestParam UUID pessoaId, UriComponentsBuilder uriBuilder) {
        VacinaResponse response = vacinaService.create(request, pessoaId);
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
    public ResponseEntity<VacinaResponse> update(@PathVariable UUID id, @RequestBody VacinaRequest request) {
        VacinaResponse response = vacinaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        vacinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaResponse;
import br.org.apae.api_crud_pacientes.domain.service.TipoDeficienciaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/tipo_deficiencia")
public class TipoDeficienciaController {
    private final TipoDeficienciaService tipoDeficienciaService;

    public TipoDeficienciaController(TipoDeficienciaService tipoDeficienciaService) {
        this.tipoDeficienciaService = tipoDeficienciaService;
    }

    @PostMapping
    public ResponseEntity<TipoDeficienciaResponse> create(
            @RequestBody TipoDeficienciaRequest request,
            @RequestParam UUID pessoaId,
            UriComponentsBuilder uriBuilder) {

        TipoDeficienciaResponse response = tipoDeficienciaService.create(request, pessoaId);
        URI uri = uriBuilder.path("/tipo_deficiencia/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDeficienciaResponse> update(@PathVariable UUID id,
                                                           @RequestBody TipoDeficienciaRequest request) {
        TipoDeficienciaResponse response = tipoDeficienciaService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<TipoDeficienciaResponse>> getAll(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String descricao) {

        Page<TipoDeficienciaResponse> tipoDeficiencias = tipoDeficienciaService.getAll(pageable, descricao);
        return ResponseEntity.ok(tipoDeficiencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDeficienciaResponse> getById(@PathVariable UUID id) {
        TipoDeficienciaResponse response = tipoDeficienciaService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        tipoDeficienciaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

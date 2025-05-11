package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.domain.service.CadastroAnualService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cadastros-anual")
public class CadastroAnualController {

    private final CadastroAnualService service;

    public CadastroAnualController(CadastroAnualService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CadastroAnualResponse> create(@Valid @RequestBody CadastroAnualRequest dto) {
        return new ResponseEntity<>(service.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CadastroAnualResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CadastroAnualResponse>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CadastroAnualResponse> update(@PathVariable Long id, @Valid @RequestBody CadastroAnualRequest dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
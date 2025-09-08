package br.org.apae.profissional_da_saude.api.controller;

import br.org.apae.profissional_da_saude.application.service.HistoricoConsultaService;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaCreateDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaResponseDTO;
import br.org.apae.profissional_da_saude.api.dto.HistoricoConsultaUpdateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/historico-consultas")
@RequiredArgsConstructor
public class HistoricoConsultaController {

    private final HistoricoConsultaService service;

    @PostMapping
    public ResponseEntity<HistoricoConsultaResponseDTO> criar(@RequestBody @Valid HistoricoConsultaCreateDTO dto) {
        HistoricoConsultaResponseDTO response = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HistoricoConsultaResponseDTO>> listarTodos() {
        List<HistoricoConsultaResponseDTO> response = service.listarTodos();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        HistoricoConsultaResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HistoricoConsultaResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid HistoricoConsultaUpdateDTO dto) {
        HistoricoConsultaResponseDTO response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
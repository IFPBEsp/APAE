package br.org.apae.api_crud_pacientes.controller;

import br.org.apae.api_crud_pacientes.DTO.request.PacienteRequest;
import br.org.apae.api_crud_pacientes.DTO.response.PacienteResponse;
import br.org.apae.api_crud_pacientes.service.PacienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> criarPaciente(
            @RequestBody PacienteRequest request,
            UriComponentsBuilder uriBuilder) {

        PacienteResponse response = pacienteService.criarPaciente(request);
        URI uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PacienteResponse>> listarPacientes(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String nome) {

        Page<PacienteResponse> pacientes = pacienteService.listarPacientes(pageable, cpf, nome);
        return ResponseEntity.ok(pacientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPacientePorId(@PathVariable UUID id) {
        PacienteResponse response = pacienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable UUID id) {
        pacienteService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
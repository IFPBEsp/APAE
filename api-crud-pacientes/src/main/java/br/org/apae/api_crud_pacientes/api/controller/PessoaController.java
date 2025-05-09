package br.org.apae.api_crud_pacientes.api.controller;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaResponse;
import br.org.apae.api_crud_pacientes.domain.service.PessoaService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {
    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponse> criarPessoa(
            @RequestBody PessoaRequest request,
            UriComponentsBuilder uriBuilder) {

        PessoaResponse response = pessoaService.criarPessoa(request);
        URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(response.getId()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponse> atualizarPessoa(@PathVariable UUID id,
                                                          @RequestBody PessoaRequest request) {
        PessoaResponse response = pessoaService.atualizarPessoa(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PessoaResponse>> listarPessoas(
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String nome) {

        Page<PessoaResponse> pessoas = pessoaService.listarPessoas(pageable, cpf, nome);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponse> buscarPessoaPorId(@PathVariable UUID id) {
        PessoaResponse response = pessoaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable UUID id) {
        pessoaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
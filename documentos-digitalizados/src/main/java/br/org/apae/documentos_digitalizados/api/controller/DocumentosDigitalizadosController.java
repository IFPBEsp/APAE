package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.AtualizarBucketRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.CriarBucketRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/documento")
public interface DocumentosDigitalizadosController {

    @PostMapping
    ResponseEntity<Void> criarBucket(@RequestBody @Valid CriarBucketRequestDTO dto);

    @GetMapping
    ResponseEntity<List<ListagemBucketResponseDTO>> listarBuckets();

    @GetMapping("/{bucket}")
    ResponseEntity<ListagemBucketResponseDTO> listarBucket(@PathVariable String bucket);

    @PutMapping
    ResponseEntity<Void> atualizarBucket(@RequestBody @Valid AtualizarBucketRequestDTO dto);

    @DeleteMapping("/{bucket}")
    ResponseEntity<Void> deletarBucket(@PathVariable String bucket);
}

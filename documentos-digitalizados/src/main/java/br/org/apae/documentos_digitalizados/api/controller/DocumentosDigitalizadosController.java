package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.BucketResponseDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

public interface DocumentosDigitalizadosController {

    @PostMapping("/{bucketNome}")
    ResponseEntity<Void> criarBucket(@PathVariable UUID bucketNome);

    @GetMapping
    ResponseEntity<ListagemBucketResponseDTO> listarBuckets();

    @GetMapping("/{bucketNome}")
    ResponseEntity<BucketResponseDTO> listarBucket(@PathVariable UUID bucketNome);

    @DeleteMapping("/{bucketNome}")
    ResponseEntity<Void> deletarBucket(@PathVariable UUID bucketNome);

    @GetMapping("/verificar/{bucketNome}")
    ResponseEntity<Boolean> verificarBucket(@PathVariable UUID bucketNome);
}

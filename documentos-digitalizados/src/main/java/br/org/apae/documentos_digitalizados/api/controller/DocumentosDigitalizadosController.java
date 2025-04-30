package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.domain.TipoDeDocumento;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.print.Pageable;

@RequestMapping("/documento")
public interface DocumentosDigitalizadosController {
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> salvarDocumento(@RequestPart("documento") DocumentosDigitalizadosRequestDTO dto,
                                         @RequestPart("anexo") MultipartFile documento);

    @GetMapping
    ResponseEntity<Page<DocumentosDigitalizadosResponseDTO>> listarDocumentos(
            @RequestParam(required = false) TipoDeDocumento tipo,
            @RequestParam(required = false) Long pacienteId,
            Pageable pageable);

    @GetMapping("/{nomeDoDocumento}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> buscarDocumentoPorNomeDoDocumento(
            @PathVariable String nomeDoDocumento);

    @PutMapping(value = "/{nomeDoDocumento}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> atualizarDocumento(@PathVariable String nomeDoDocumento,
                                            @RequestPart("documento") DocumentosDigitalizadosRequestDTO dto,
                                            @RequestPart("anexo") MultipartFile documento);

    @DeleteMapping("/{nomeDoDocumento}")
    ResponseEntity<Void> removerDocumento(@PathVariable String nomeDoDocumento);
}

package br.org.apae.documentos_escolares.api.controller;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface DocumentosEscolaresController {
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> anexarDocumentoEscolar(
            @RequestPart("dto") DocumentoEscolarUploadRequestDTO dto,
            @RequestPart("arquivo") MultipartFile arquivo
    );

    @GetMapping("/{pacienteId}/filtrar")
    ResponseEntity<DocumentoEscolarResponseDTO> listarDocumentosEscolares(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam(value = "ano", required = false) Integer ano
    );

    @GetMapping("/{pacienteId}/historico")
    ResponseEntity<DocumentoEscolarResponseDTO> historicoDocumentoEscolares(
            @PathVariable("pacienteId") UUID pacienteId
    );

    @GetMapping("/{pacienteId}/visualizar")
    ResponseEntity<byte[]> visualizarDocumentoEscolar(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam String nomeArquivo
    );

    @DeleteMapping("/{pacienteId}")
    ResponseEntity<Void> deletarDocumentoEscolar(
            @PathVariable("pacienteId") UUID pacienteId,
            @RequestParam String nomeArquivo
    );
}

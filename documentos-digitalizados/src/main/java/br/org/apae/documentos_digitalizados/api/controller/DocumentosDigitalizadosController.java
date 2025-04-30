package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/documento")
public interface DocumentosDigitalizadosController {
    @PostMapping
    ResponseEntity<Void> salvarDocumento(DocumentosDigitalizadosRequestDTO dto,
                                         MultipartFile documento);

    @GetMapping
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarTodosDocumentos();

    @GetMapping("/pessoal")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarTodosDocumentosPessoal();

    @GetMapping("/medico")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarTodosDocumentosMedico();

    @GetMapping("/escolar")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarTodosDocumentosEscolar();

    @GetMapping("/{pacienteId}")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarTodosDocumentosPorPaciente(@PathVariable Long pacienteId);

    @GetMapping("/pessoal/{pacienteId}")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarDocumentosPessoalPorPaciente(@PathVariable Long pacienteId);

    @GetMapping("/medico/{pacienteId}")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarDocumentosMedicoPorPaciente(@PathVariable Long pacienteId);

    @GetMapping("/escolar/{pacienteId}")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarDocumentosEscolarPorPaciente(@PathVariable Long pacienteId);

    @PutMapping("/{nomeDoDocumento}")
    ResponseEntity<Void> atualizarDocumento(@PathVariable String nomeDoDocumento,
                                            DocumentosDigitalizadosRequestDTO dto,
                                            MultipartFile documento);

    @DeleteMapping("/{nomeDoDocumento}")
    ResponseEntity<Void> removerDocumento(@PathVariable String nomeDoDocumento);
}

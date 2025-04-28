package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/pacientes")
public interface DocumentosDigitalizadosController {


    @GetMapping("/{id_paciente}/documentos")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarDocumentosPaciente(
            @PathVariable Long id_paciente);


    @GetMapping("/{id_paciente}/laudos_medico/documentos")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarLaudosMedicos(
            @PathVariable Long id_paciente);


    @PostMapping("/{id_paciente}/laudos_medico/documentos")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> uploadLaudoMedico(
            @PathVariable Long id_paciente,
            @RequestParam("file") MultipartFile file,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO);

    @GetMapping("/{id_paciente}/laudos_medico/documentos/{id}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> obterLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable Long id);

    @GetMapping("/{id_paciente}/documentos/{id}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> obterDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id);

    @PostMapping("/{id_paciente}/documentos")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> uploadDocumento(
            @PathVariable Long id_paciente,
            @RequestParam("file") MultipartFile file,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO);

    @PutMapping("/{id_paciente}/documentos/{id}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> atualizarDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO);

    @DeleteMapping("/{id_paciente}/documentos/{id}")
    ResponseEntity<Void> removerDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id);
}

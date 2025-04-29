package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {

    @Override
    @GetMapping("/{id_paciente}/documentos")
    public ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarDocumentosPaciente(
            @PathVariable Long id_paciente) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/laudos_medico/documentos")
    public ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarLaudosMedicos(
            @PathVariable Long id_paciente) {
        return null;
    }

    @Override
    @PostMapping("/{id_paciente}/laudos_medico/documentos")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> uploadLaudoMedico(
            @PathVariable Long id_paciente,
            @RequestParam("file") MultipartFile file,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/laudos_medico/documentos/{id}")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> obterLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable Long id) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/documentos/{id}")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> obterDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id) {
        return null;
    }

    @Override
    @PostMapping("/{id_paciente}/documentos")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> uploadDocumento(
            @PathVariable Long id_paciente,
            @RequestParam("file") MultipartFile file,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO) {
        return null;
    }

    @Override
    @PutMapping("/{id_paciente}/documentos/{id}")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> atualizarDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id,
            @RequestBody DocumentosDigitalizadosRequestDTO requestDTO) {
        return null;
    }

    @Override
    @DeleteMapping("/{id_paciente}/documentos/{id}")
    public ResponseEntity<Void> removerDocumento(
            @PathVariable Long id_paciente,
            @PathVariable Long id) {
        return null;
    }
}
package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/pacientes")
public interface DocumentosDigitalizadosController {

    @GetMapping("/{id_paciente}/documentos")
    ResponseEntity<List<MultipartFile>> listarDocumentosPaciente(
            @PathVariable Long id_paciente
    );

    @GetMapping("/{id_paciente}/laudos_medico/documentos")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarLaudosMedicos(
            @PathVariable Long id_paciente
    );

    @GetMapping("/{id_paciente}/encaminhamento/documentos")
    ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarEncaminhamentos(
            @PathVariable Long id_paciente
    );

    @GetMapping("/{id_paciente}/laudos_medico/documentos/{uuid}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> obterLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );

    @GetMapping("/{id_paciente}/encaminhamento/documentos/{uuid}")
    ResponseEntity<DocumentosDigitalizadosResponseDTO> obterEncaminhamento(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );

    @PostMapping(value = "/{id_paciente}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> salvarDocumentosPaciente(
            @PathVariable Long id_paciente,
            @RequestPart("encaminhamento") MultipartFile encaminhamento,
            @RequestPart("laudo_medico") MultipartFile laudoMedico
    );

    @PostMapping(value = "/{id_paciente}/laudo_medico/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadLaudoMedico(
            @PathVariable Long id_paciente,
            @RequestParam("laudo_medico") MultipartFile file
    );

    @PostMapping(value = "/{id_paciente}/encaminhamento/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> uploadEncaminhamento(
            @PathVariable Long id_paciente,
            @RequestParam("encaminhamento") MultipartFile file
    );

    @PutMapping(value = "/{id_paciente}/laudo_medico/documentos/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> atualizarLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid,
            @RequestParam("laudo_medico") MultipartFile file
    );

    @PutMapping(value = "/{id_paciente}/encaminhamento/documentos/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> atualizarEncaminhamento(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid,
            @RequestParam("encaminhamento") MultipartFile file
    );

    @DeleteMapping("/{id_paciente}/laudo_medico/documentos/{uuid}")
    ResponseEntity<Void> removerLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );

    @DeleteMapping("/{id_paciente}/encaminhamento/documentos/{uuid}")
    ResponseEntity<Void> removerEncaminhamento(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );
}

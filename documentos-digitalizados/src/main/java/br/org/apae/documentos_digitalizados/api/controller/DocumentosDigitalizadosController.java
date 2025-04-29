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

    @GetMapping("/{id_paciente}/laudos_medico/documentos/{uuid}")
    ResponseEntity<MultipartFile> obterLaudoMedico(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );

    @GetMapping("/{id_paciente}/encaminhamento/documentos/{uuid}")
    ResponseEntity<MultipartFile> obterEncaminhamento(
            @PathVariable Long id_paciente,
            @PathVariable UUID uuid
    );

    @PostMapping(value = "/{id_paciente}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Void> salvarDocumentosPaciente(
            @PathVariable Long id_paciente,
            @RequestPart("encaminhamento") MultipartFile encaminhamento,
            @RequestPart("laudo_medico") MultipartFile laudoMedico
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

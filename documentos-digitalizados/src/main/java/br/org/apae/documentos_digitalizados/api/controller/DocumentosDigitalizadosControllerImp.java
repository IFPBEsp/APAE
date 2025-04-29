package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pacientes")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {

    @Override
    @GetMapping("/{id_paciente}/documentos")
    public ResponseEntity<List<MultipartFile>> listarDocumentosPaciente(Long id_paciente) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/laudos_medico/documentos")
    public ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarLaudosMedicos(Long id_paciente) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/encaminhamento/documentos")
    public ResponseEntity<List<DocumentosDigitalizadosResponseDTO>> listarEncaminhamentos(Long id_paciente) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/laudos_medico/documentos/{uuid}")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> obterLaudoMedico(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    @GetMapping("/{id_paciente}/encaminhamento/documentos/{uuid}")
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> obterEncaminhamento(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    @PostMapping(value = "/{id_paciente}/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> salvarDocumentosPaciente(Long id_paciente, MultipartFile encaminhamento, MultipartFile laudoMedico) {
        return null;
    }

    @Override
    @PostMapping(value = "/{id_paciente}/laudo_medico/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadLaudoMedico(Long id_paciente, MultipartFile file) {
        return null;
    }

    @Override
    @PostMapping(value = "/{id_paciente}/encaminhamento/documentos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadEncaminhamento(Long id_paciente, MultipartFile file) {
        return null;
    }

    @Override
    @PutMapping(value = "/{id_paciente}/laudo_medico/documentos/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> atualizarLaudoMedico(Long id_paciente, UUID uuid, MultipartFile file) {
        return null;
    }

    @Override
    @PutMapping(value = "/{id_paciente}/encaminhamento/documentos/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> atualizarEncaminhamento(Long id_paciente, UUID uuid, MultipartFile file) {
        return null;
    }

    @Override
    @DeleteMapping("/{id_paciente}/laudo_medico/documentos/{uuid}")
    public ResponseEntity<Void> removerLaudoMedico(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    @DeleteMapping("/{id_paciente}/encaminhamento/documentos/{uuid}")
    public ResponseEntity<Void> removerEncaminhamento(Long id_paciente, UUID uuid) {
        return null;
    }
}
package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.service.DocumentosDigitalidadosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {

    private final DocumentosDigitalidadosService documentosDigitalidadosService;

    @Override
    public ResponseEntity<List<MultipartFile>> listarDocumentosPaciente(Long id_paciente) {
        return null;
    }

    @Override
    public ResponseEntity<MultipartFile> obterLaudoMedico(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    public ResponseEntity<MultipartFile> obterEncaminhamento(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    public ResponseEntity<Void> salvarDocumentosPaciente(Long id_paciente, MultipartFile encaminhamento, MultipartFile laudoMedico) {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizarLaudoMedico(Long id_paciente, UUID uuid, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizarEncaminhamento(Long id_paciente, UUID uuid, MultipartFile file) {
        return null;
    }

    @Override
    public ResponseEntity<Void> removerLaudoMedico(Long id_paciente, UUID uuid) {
        return null;
    }

    @Override
    public ResponseEntity<Void> removerEncaminhamento(Long id_paciente, UUID uuid) {
        return null;
    }
}
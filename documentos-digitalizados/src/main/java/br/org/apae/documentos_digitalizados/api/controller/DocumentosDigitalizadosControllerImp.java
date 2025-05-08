package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {



    @Override
    public ResponseEntity<Void> salvarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento) throws Exception {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ListagemBucketResponseDTO> listarDocumentos(ListagemBucketRequestDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<PacienteDocumentoResponseDTO> buscarPorPaciente(Long pacienteID) {
        return null;
    }

    @Override
    public ResponseEntity<DocumentosDigitalizadosResponseDTO> buscarDocumento(BuscaDocumentoRequestDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento) {
        return null;
    }
}
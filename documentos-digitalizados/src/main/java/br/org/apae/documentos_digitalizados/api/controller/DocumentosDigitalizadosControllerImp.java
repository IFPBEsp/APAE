package br.org.apae.documentos_digitalizados.api.controller;

import br.org.apae.documentos_digitalizados.api.dto.AtualizarBucketRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.CriarBucketRequestDTO;
import br.org.apae.documentos_digitalizados.api.dto.ListagemBucketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pacientes")
public class DocumentosDigitalizadosControllerImp implements DocumentosDigitalizadosController {
    @Override
    public ResponseEntity<Void> criarBucket(CriarBucketRequestDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<List<ListagemBucketResponseDTO>> listarBuckets() {
        return null;
    }

    @Override
    public ResponseEntity<ListagemBucketResponseDTO> listarBucket(String bucket) {
        return null;
    }

    @Override
    public ResponseEntity<Void> atualizarBucket(AtualizarBucketRequestDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletarBucket(String bucket) {
        return null;
    }
}
package br.org.apae.documentos_escolares.api.controller;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.application.service.DocumentosEscolaresService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documentos-escolares")
public class DocumentosEscolaresControllerImp implements DocumentosEscolaresController {

    private final DocumentosEscolaresService documentosEscolaresService;

    @Override
    public ResponseEntity<Void> anexarDocumentoEscolar(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        documentosEscolaresService.salvarArquivo(dto, arquivo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<DocumentoEscolarResponseDTO> listarDocumentosEscolares(UUID pacienteId, Integer ano) {
        DocumentoEscolarResponseDTO listaDocumentosEscolares = documentosEscolaresService.listarDocumentosEscolares(pacienteId);
        if (ano != null) {
            listaDocumentosEscolares = documentosEscolaresService.listarDocumentosEscolaresAno(pacienteId, ano);
        }

        return ResponseEntity.status(HttpStatus.OK).body(listaDocumentosEscolares);
    }

    @Override
    public ResponseEntity<DocumentoEscolarResponseDTO> historicoDocumentoEscolares(UUID pacienteId) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        return null;
    }
}

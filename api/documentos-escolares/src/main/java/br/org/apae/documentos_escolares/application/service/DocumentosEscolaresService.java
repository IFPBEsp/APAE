package br.org.apae.documentos_escolares.application.service;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface DocumentosEscolaresService {
    void salvarArquivo(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo);

    DocumentoEscolarResponseDTO listarDocumentosEscolares(UUID pacienteId);

    DocumentoEscolarResponseDTO listarDocumentosEscolaresAno(UUID pacienteId, Integer ano);

    DocumentoEscolarResponseDTO historicoDocumentosEscolares(UUID pacienteId);

    byte[] visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo);

    void deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo);
}

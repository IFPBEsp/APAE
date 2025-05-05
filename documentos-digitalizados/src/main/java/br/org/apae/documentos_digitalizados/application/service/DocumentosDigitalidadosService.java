package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import io.minio.StatObjectResponse;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentosDigitalidadosService {
    @Transactional
    void salvarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento) throws Exception;

    ListagemBucketResponseDTO listarDocumentos(ListagemBucketRequestDTO dto);

    PacienteDocumentoResponseDTO buscarPaciente(UUID idPaciente);

    DocumentosDigitalizadosResponseDTO downloadDocumento(BuscaDocumentoRequestDTO dto);

    StatObjectResponse metadadoDocumento(BuscaDocumentoRequestDTO dto);

    @Transactional
    void atualizarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento);
}

package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentosDigitalidadosService {
    @Transactional
    void salvarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento);

    List<String> listarDocumentos(ListagemBucketRequestDTO dto);

    List<PacienteDocumentoResponseDTO> listarPaciente(Long idPaciente);

    DocumentosDigitalizadosResponseDTO downloadDocumento(BuscaDocumentoRequestDTO dto);

    @Transactional
    void atualizarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento);
}

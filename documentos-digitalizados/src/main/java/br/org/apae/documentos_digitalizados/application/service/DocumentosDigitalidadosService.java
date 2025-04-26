package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;

import java.util.List;

public interface DocumentosDigitalidadosService {
    DocumentosDigitalizadosResponseDTO criar(DocumentosDigitalizadosRequestDTO dto);
    DocumentosDigitalizadosResponseDTO buscarPorId(Long id);
    List<DocumentosDigitalizadosResponseDTO> listarTodos();
    DocumentosDigitalizadosResponseDTO atualizar(Long id, DocumentosDigitalizadosRequestDTO dto);
    void excluir(Long id);
}

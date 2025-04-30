package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;

import java.util.List;

public interface DocumentosDigitalidadosService {
    void criar(DocumentosDigitalizadosRequestDTO dto);
    DocumentosDigitalizados buscarPorId(Long id);
    List<DocumentosDigitalizados> listarTodos();
    void atualizar(Long id, DocumentosDigitalizadosRequestDTO dto);
    void excluir(Long id);
    DocumentosDigitalizados buscarPorPaciente(Long id);
}

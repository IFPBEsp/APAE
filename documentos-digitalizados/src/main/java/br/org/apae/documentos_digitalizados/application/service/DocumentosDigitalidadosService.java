package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;

import java.util.List;

public interface DocumentosDigitalidadosService {
    DocumentosDigitalizados salvarDocumento(DocumentosDigitalizadosRequestDTO dto);

    List<DocumentosDigitalizados> listarTodosDocumentos();

    List<DocumentosDigitalizados> listarTodosDocumentosPessoal();

    List<DocumentosDigitalizados> listarTodosDocumentosMedico();

    List<DocumentosDigitalizados> listarTodosDocumentosEscolar();

    List<DocumentosDigitalizados> listarTodosDocumentosPorPaciente(Long pacienteId);

    List<DocumentosDigitalizados> listarDocumentosPessoalPorPaciente(Long pacienteId);

    List<DocumentosDigitalizados> listarDocumentosMedicoPorPaciente(Long pacienteId);

    List<DocumentosDigitalizados> listarDocumentosEscolarPorPaciente(Long pacienteId);

    void atualizarDocumento(String nomeDoDocumento, DocumentosDigitalizadosRequestDTO dto);

    void removerDocumento(String nomeDoDocumento);
}

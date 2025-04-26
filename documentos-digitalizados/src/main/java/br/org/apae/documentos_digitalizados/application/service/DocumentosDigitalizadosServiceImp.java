package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentosDigitalizadosServiceImp implements DocumentosDigitalidadosService {
    @Override
    public DocumentosDigitalizadosResponseDTO criar(DocumentosDigitalizadosRequestDTO dto) {
        return null;
    }

    @Override
    public DocumentosDigitalizadosResponseDTO buscarPorId(Long id) {
        return null;
    }

    @Override
    public List<DocumentosDigitalizadosResponseDTO> listarTodos() {
        return List.of();
    }

    @Override
    public DocumentosDigitalizadosResponseDTO atualizar(Long id, DocumentosDigitalizadosRequestDTO dto) {
        return null;
    }

    @Override
    public void excluir(Long id) {

    }
}

package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.application.mapper.DocumentoDigitalizadosMapper;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import br.org.apae.documentos_digitalizados.infrastructure.repository.DocumentosDigitalizadosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentosDigitalizadosServiceImp implements DocumentosDigitalidadosService {

    private final DocumentosDigitalizadosRepository documentosDigitalizadosRepository;
    private final DocumentoDigitalizadosMapper documentoDigitalizadosMapper;

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
        List<DocumentosDigitalizados> documentosDigitalizados = documentosDigitalizadosRepository.findAll();
        return documentoDigitalizadosMapper.toDTO(documentosDigitalizados);
    }

    @Override
    public DocumentosDigitalizadosResponseDTO atualizar(Long id, DocumentosDigitalizadosRequestDTO dto) {
        return null;
    }

    @Override
    public void excluir(Long id) {

    }
}

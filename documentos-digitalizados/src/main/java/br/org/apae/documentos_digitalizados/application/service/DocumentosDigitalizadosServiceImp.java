package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosRequestDTO;
import br.org.apae.documentos_digitalizados.application.dtos.DocumentosDigitalizadosResponseDTO;
import br.org.apae.documentos_digitalizados.application.exception.DocumentoDigitalizadoNaoEncontradoException;
import br.org.apae.documentos_digitalizados.application.mapper.DocumentoDigitalizadosMapper;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import br.org.apae.documentos_digitalizados.infrastructure.repository.DocumentosDigitalizadosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentosDigitalizadosServiceImp implements DocumentosDigitalidadosService {

    private final DocumentosDigitalizadosRepository repository;
    private final DocumentoDigitalizadosMapper mapper;

    @Override
    public DocumentosDigitalizadosResponseDTO criar(DocumentosDigitalizadosRequestDTO dto) {
        DocumentosDigitalizados documentoDigitalizado = mapper.toEntity(dto);
        documentoDigitalizado = repository.save(documentoDigitalizado);

        return mapper.toDTO(documentoDigitalizado);
    }

    @Override
    public DocumentosDigitalizadosResponseDTO buscarPorId(Long id) {
        DocumentosDigitalizados documentoDigitalizado = repository.findById(id).orElseThrow(() -> new DocumentoDigitalizadoNaoEncontradoException("Documento digitalizado não encontrado!"));
        return mapper.toDTO(documentoDigitalizado);
    }

    @Override
    public List<DocumentosDigitalizadosResponseDTO> listarTodos() {
        List<DocumentosDigitalizados> documentosDigitalizados = repository.findAll();
        return mapper.toDTO(documentosDigitalizados);
    }

    @Override
    public DocumentosDigitalizadosResponseDTO atualizar(Long id, DocumentosDigitalizadosRequestDTO dto) {
        return null;
    }

    @Override
    public void excluir(Long id) {

    }
}

package br.org.apae.documentos_escolares.application.service;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.api.dto.response.UrlPreAssinadaResponseDTO;
import br.org.apae.documentos_escolares.application.service.exceptions.DocumentoEscolarNaoEncontradoException;
import br.org.apae.documentos_escolares.application.service.exceptions.ErroAoSalvarDocumentoException;
import br.org.apae.documentos_escolares.domain.exception.UploadDocumentoException;
import br.org.apae.documentos_escolares.domain.model.DocumentosEscolares;
import br.org.apae.documentos_escolares.domain.repository.DocumentosEscolaresRepository;
import br.org.apae.documentos_escolares.application.service.minio.MinioStorageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DocumentosEscolaresServiceImpl implements DocumentosEscolaresService {

    private final DocumentosEscolaresRepository documentosEscolaresRepository;
    private final MinioStorageService minioStorageService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void salvarArquivo(@Valid DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        validarArquivo(arquivo);

        try {
            minioStorageService.salvarArquivo(dto, arquivo);
        } catch (UploadDocumentoException e) {
            throw e;
        } catch (Exception e) {
            throw new ErroAoSalvarDocumentoException("Erro ao salvar documento no storage: " + e.getMessage());
        }

        DocumentosEscolares doc = DocumentosEscolares.builder()
                .pacienteId(dto.pacienteId())
                .ano(dto.ano())
                .nomeArquivo(arquivo.getOriginalFilename())
                .tipoArquivo(arquivo.getContentType())
                .caminhoArquivo("documentos-escolar/" + dto.ano() + "/" + arquivo.getOriginalFilename())
                .dataUpload(LocalDateTime.now())
                .build();

        try {
            documentosEscolaresRepository.save(doc);
        } catch (Exception e) {
            try {
                minioStorageService.deletarDocumentoEscolar(dto.pacienteId(), arquivo.getOriginalFilename());
            } catch (Exception ex) {
                System.err.println("Falha ao deletar arquivo após erro no banco: " + ex.getMessage());
            }
            throw new ErroAoSalvarDocumentoException("Erro ao salvar metadados do documento no banco: " + e.getMessage());
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo == null || arquivo.isEmpty()) {
            throw new ErroAoSalvarDocumentoException("Arquivo não pode ser vazio");
        }
        String contentType = arquivo.getContentType();
        if (contentType == null || !(contentType.equals("application/pdf") || contentType.startsWith("image/"))) {
            throw new ErroAoSalvarDocumentoException("Tipo de arquivo não suportado. Envie PDF ou imagens.");
        }
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolares(UUID pacienteId) {
        List<DocumentosEscolares> documentos = documentosEscolaresRepository.findByPacienteId(pacienteId);
        if (documentos.isEmpty()) {
            throw new DocumentoEscolarNaoEncontradoException("Nenhum documento encontrado para paciente: " + pacienteId);
        }
        return mapParaResponse(pacienteId, documentos);
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolaresAno(UUID pacienteId, Integer ano) {
        List<DocumentosEscolares> documentos = documentosEscolaresRepository.findByPacienteIdAndAno(pacienteId, ano);
        if (documentos.isEmpty()) {
            throw new DocumentoEscolarNaoEncontradoException("Nenhum documento encontrado para paciente: " + pacienteId + " no ano: " + ano);
        }
        return mapParaResponse(pacienteId, documentos);
    }

    @Override
    public DocumentoEscolarResponseDTO historicoDocumentosEscolares(UUID pacienteId) {
        return listarDocumentosEscolares(pacienteId);
    }

    @Override
    public byte[] visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        var docOpt = documentosEscolaresRepository.findByPacienteIdAndNomeArquivo(pacienteId, nomeArquivo);
        if (docOpt.isEmpty()) {
            throw new DocumentoEscolarNaoEncontradoException("Documento não encontrado: " + nomeArquivo);
        }
        return minioStorageService.visualizarDocumentoEscolar(pacienteId, nomeArquivo);
    }

    @Override
    @Transactional
    public void deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        var docOpt = documentosEscolaresRepository.findByPacienteIdAndNomeArquivo(pacienteId, nomeArquivo);
        if (docOpt.isEmpty()) {
            throw new DocumentoEscolarNaoEncontradoException("Documento não encontrado para exclusão: " + nomeArquivo);
        }
        minioStorageService.deletarDocumentoEscolar(pacienteId, nomeArquivo);
        documentosEscolaresRepository.deleteByPacienteIdAndNomeArquivo(pacienteId, nomeArquivo);
    }

    private DocumentoEscolarResponseDTO mapParaResponse(UUID pacienteId, List<DocumentosEscolares> documentos) {
        List<UrlPreAssinadaResponseDTO> urls = documentos.stream()
                .map(doc -> new UrlPreAssinadaResponseDTO(
                        doc.getNomeArquivo(),
                        doc.getCaminhoArquivo()
                ))
                .toList();

        return new DocumentoEscolarResponseDTO(pacienteId, urls);
    }
}

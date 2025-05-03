package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import br.org.apae.documentos_digitalizados.application.exception.DocumentoDigitalizadoNaoEncontradoException;
import br.org.apae.documentos_digitalizados.application.mapper.DocumentoDigitalizadosMapper;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import br.org.apae.documentos_digitalizados.infrastructure.repository.DocumentosDigitalizadosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentosDigitalizadosServiceImp implements DocumentosDigitalidadosService {

    private final DocumentosDigitalizadosRepository repository;
    private final DocumentoDigitalizadosMapper mapper;
    private final MinioStorageService minioStorageService;


    @Override
    public void salvarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile file) {
        DocumentosDigitalizados documento = mapper.toEntity(dto, file);
        repository.save(documento);

        minioStorageService.criarBucket(documento.getNomeBucket(), documento.getTipoPaciente());
        minioStorageService.uploadDocumento(documento.getNomeBucket(), documento.getTipoDocumento(), documento.getDocumento(), file);
    }

    @Override
    public ListagemBucketResponseDTO listarDocumentos(ListagemBucketRequestDTO dto) {
        String nomeBucket = mapper.nomeBucket(dto);
        String subBucket = mapper.subBucket(dto);

        List<String> listagem = minioStorageService.listarDocumentos(nomeBucket, subBucket);
        return mapper.toListagem(listagem);
    }

    @Override
    public PacienteDocumentoResponseDTO buscarPaciente(Long idPaciente) {
        DocumentosDigitalizados documento = repository.findByPacienteId(idPaciente).orElseThrow(() -> new DocumentoDigitalizadoNaoEncontradoException("Paciênte não encontrado!"));

        return mapper.toPaciente(documento);
    }

    @Override
    public DocumentosDigitalizadosResponseDTO downloadDocumento(BuscaDocumentoRequestDTO dto) {
        return null;
    }

    @Override
    public void atualizarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile documento) {
        
    }
}

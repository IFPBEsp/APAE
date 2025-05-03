package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.dtos.*;
import br.org.apae.documentos_digitalizados.application.exception.DocumentoDigitalizadoNaoEncontradoException;
import br.org.apae.documentos_digitalizados.application.mapper.DocumentoDigitalizadosMapper;
import br.org.apae.documentos_digitalizados.domain.DocumentosDigitalizados;
import br.org.apae.documentos_digitalizados.infrastructure.repository.DocumentosDigitalizadosRepository;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentosDigitalizadosServiceImp implements DocumentosDigitalidadosService {

    private final DocumentosDigitalizadosRepository repository;
    private final DocumentoDigitalizadosMapper mapper;
    private final MinioStorageService minioStorageService;


    @Override
    public void salvarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile arquivo) {
        DocumentosDigitalizados documento = mapper.toEntity(dto);
        repository.save(documento);

        String nomeDocumento = mapper.nomeDocumento(dto, arquivo);

        minioStorageService.criarBucket(documento.getNomeBucket(), documento.getTipoPaciente());
        minioStorageService.uploadDocumento(documento.getNomeBucket(), documento.getTipoDocumento(), nomeDocumento, arquivo);
    }

    @Override
    public ListagemBucketResponseDTO listarDocumentos(ListagemBucketRequestDTO dto) {
        String nomeBucket = mapper.nomeBucket(dto.idPaciente(), dto.nomePaciente());
        String subBucket = mapper.subBucket(dto.tipoDocumento().getTipo());

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
        String caminho = mapper.toCaminho(dto.tipoDocumento().getTipo(), dto.nomeDocumento());
        String nomeBucket = mapper.nomeBucket(dto.idPaciente(), dto.nomePaciente());

        InputStream documento = minioStorageService.downloadDocumento(nomeBucket, caminho);
        StatObjectResponse metadado = minioStorageService.metadadoDocumento(nomeBucket, caminho);

        return new DocumentosDigitalizadosResponseDTO(new InputStreamResource(documento));
    }

    @Override
    public StatObjectResponse metadadoDocumento(BuscaDocumentoRequestDTO dto) {
        String caminho = mapper.toCaminho(dto.tipoDocumento().getTipo(), dto.nomeDocumento());
        String nomeBucket = mapper.nomeBucket(dto.idPaciente(), dto.nomePaciente());

        return minioStorageService.metadadoDocumento(nomeBucket, caminho);
    }

    @Override
    public void atualizarDocumento(DocumentosDigitalizadosRequestDTO dto, MultipartFile arquivo) {
        if (repository.findByPacienteId(dto.pacienteId()).isPresent()) {
            DocumentosDigitalizados documento = mapper.toEntity(dto);
            repository.save(documento);

            String nomeDocumento = mapper.nomeDocumento(dto, arquivo);

            minioStorageService.atualizarDocumento(documento.getNomeBucket(), documento.getTipoDocumento(), nomeDocumento, arquivo);
        }
    }
}

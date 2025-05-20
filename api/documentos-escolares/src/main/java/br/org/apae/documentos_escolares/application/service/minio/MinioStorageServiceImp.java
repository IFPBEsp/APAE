package br.org.apae.documentos_escolares.application.service.minio;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUpdateRequestDTO;
import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.domain.exception.ArquivoVazioException;
import br.org.apae.documentos_escolares.domain.exception.BucketNaoExisteException;
import br.org.apae.documentos_escolares.domain.exception.DocumentoEscolarException;
import br.org.apae.documentos_escolares.domain.exception.UploadDocumentoException;
import br.org.apae.documentos_escolares.infrastructure.client.StorageClient;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final StorageClient storageClient;
    private final MinioClient minioClient;
    private static final String NOME_ARQUIVO = "documentos-escolar";

    @Override
    public void salvarArquivo(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        validarBucket(dto.pacienteId().toString());
        validarArquivo(arquivo);
        String caminho = NOME_ARQUIVO + "/" + dto.ano() + "/" + arquivo.getOriginalFilename();

        try {
            byte[] arquivoBytes = arquivo.getBytes();
            InputStream arquivoInputStream = new ByteArrayInputStream(arquivoBytes);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.pacienteId().toString())
                            .object(caminho)
                            .stream(arquivoInputStream, arquivoBytes.length, -1)
                            .contentType("application/octet-stream")
                            .build()
            );
        } catch (IOException e) {
            throw new DocumentoEscolarException("Erro ao processar o arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new UploadDocumentoException("Erro ao fazer upload do documento: " + e.getMessage());
        }
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolares(UUID pacienteId) {
        return null;
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolaresAno(UUID pacienteId, Integer ano) {
        return null;
    }

    @Override
    public DocumentoEscolarResponseDTO historicoDocumentosEscolares(UUID pacienteId) {
        return null;
    }

    @Override
    public byte[] visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        return new byte[0];
    }

    @Override
    public void atualizarDocumento(DocumentoEscolarUpdateRequestDTO dto, MultipartFile arquivo) {

    }

    @Override
    public void deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {

    }

    private void validarBucket(String bucketNome) {
        if (!storageClient.existeBucket(bucketNome)) {
            throw new BucketNaoExisteException("Bucket \"" + bucketNome + "\" não existe.");
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            throw new ArquivoVazioException("Não é possível fazer upload de um arquivo vazio.");
        }
    }
}

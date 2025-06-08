package br.org.apae.documentos_escolares.application.service.minio;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUpdateRequestDTO;
import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.api.dto.response.UrlPreAssinadaResponseDTO;
import br.org.apae.documentos_escolares.domain.exception.*;
import br.org.apae.documentos_escolares.infrastructure.client.StorageClient;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final StorageClient storageClient;
    private final MinioClient minioClient;
    private static final String PASTA_DOCUMENTOS = "documentos-escolar";

    @Override
    public void salvarArquivo(DocumentoEscolarUploadRequestDTO dto, MultipartFile arquivo) {
        validarBucket(dto.pacienteId());
        validarArquivo(arquivo);

        String caminho = PASTA_DOCUMENTOS + "/" + dto.ano() + "/" + arquivo.getOriginalFilename();

        uploadArquivo(caminho, dto.pacienteId().toString(), arquivo);
    }

    private DocumentoEscolarResponseDTO listarDocumentosComPrefixo(UUID pacienteId, String prefix) {
        String bucket = pacienteId.toString();
        List<UrlPreAssinadaResponseDTO> urls = new ArrayList<>();

        try {
            Iterable<Result<Item>> objetos = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(prefix)
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : objetos) {
                Item item = result.get();

                if (!item.isDir()) {
                    String objectName = item.objectName();
                    String fileName = objectName.substring(objectName.lastIndexOf('/') + 1);

                    if (!fileName.isEmpty()){
                        String link = minioClient.getPresignedObjectUrl(
                                GetPresignedObjectUrlArgs.builder()
                                        .bucket(bucket)
                                        .object(item.objectName())
                                        .method(Method.GET)
                                        .expiry(60 * 60)
                                        .build()
                        );
                        urls.add(new UrlPreAssinadaResponseDTO(fileName, link));
                    }
                }
            }
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro ao listar documentos: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao listar documentos: " + e.getMessage());
        }

        return new DocumentoEscolarResponseDTO(pacienteId, urls);
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolares(UUID pacienteId) {
        validarBucket(pacienteId);

        String prefix = PASTA_DOCUMENTOS + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolaresAno(UUID pacienteId, Integer ano) {
        validarBucket(pacienteId);

        String prefix = PASTA_DOCUMENTOS + "/" + ano + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }

    @Override
    public DocumentoEscolarResponseDTO historicoDocumentosEscolares(UUID pacienteId) {
        validarBucket(pacienteId);

        String prefix = PASTA_DOCUMENTOS + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }


    @Override
    public byte[] visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        validarBucket(pacienteId);

        String prefix = PASTA_DOCUMENTOS + "/";
        DocumentoEscolarResponseDTO documentos = listarDocumentosComPrefixo(pacienteId, prefix);

        UrlPreAssinadaResponseDTO documentoEncontrado = documentos.urls().stream()
                .filter(doc -> doc.fileName().equals(nomeArquivo))
                .findFirst()
                .orElseThrow(() -> new DocumentoEscolarNaoEncontradoException("Arquivo não encontrado: " + nomeArquivo));

        String url = documentoEncontrado.link();
        String objectName = extrairObjectNameDaUrl(url, pacienteId.toString());

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(pacienteId.toString())
                        .object(objectName)
                        .build())) {
            return is.readAllBytes();
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro no serviço de armazenamento: " + e.getMessage());
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new DocumentoEscolarException("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    @Override
    public void atualizarDocumento(DocumentoEscolarUpdateRequestDTO dto, MultipartFile arquivo) {
        validarBucket(dto.pacienteId());
        validarArquivo(arquivo);

        String caminho = PASTA_DOCUMENTOS + "/" + dto.ano() + "/" + dto.documentoNome();
        validarArquivoExistente(caminho, dto.pacienteId().toString());

        uploadArquivo(caminho, dto.pacienteId().toString(), arquivo);
    }

    @Override
    public void deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        validarBucket(pacienteId);

        String bucket = pacienteId.toString();
        String prefix = PASTA_DOCUMENTOS + "/";
        DocumentoEscolarResponseDTO documentos = listarDocumentosComPrefixo(pacienteId, prefix);

        UrlPreAssinadaResponseDTO documentoEncontrado = documentos.urls().stream()
                .filter(doc -> doc.fileName().equals(nomeArquivo))
                .findFirst()
                .orElseThrow(() -> new DocumentoEscolarNaoEncontradoException("Arquivo não encontrado: " + nomeArquivo));

        String url = documentoEncontrado.link();
        String objectName = extrairObjectNameDaUrl(url, pacienteId.toString());

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro ao deletar arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao deletar arquivo: " + e.getMessage());
        }
    }

    private void uploadArquivo(String caminho, String bucketNome, MultipartFile arquivo) {
        try (InputStream arquivoInputStream = arquivo.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(caminho)
                            .stream(arquivoInputStream, arquivo.getSize(), -1)
                            .contentType(getContentType(arquivo.toString()))
                            .build()
            );

        } catch (IOException e) {
            throw new DocumentoEscolarException("Erro ao processar o arquivo: " + e.getMessage());
        } catch (MinioException e) {
            throw new UploadDocumentoException("Erro ao fazer upload do documento: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao salvar arquivo: " + e.getMessage());
        }
    }

    private void validarBucket(UUID bucketNome) {
        if (!storageClient.existeBucket(bucketNome)) {
            throw new BucketNaoExisteException("Bucket \"" + bucketNome + "\" não existe.");
        }
    }

    private void validarArquivo(MultipartFile arquivo) {
        if (arquivo.isEmpty()) {
            throw new ArquivoVazioException("Não é possível fazer upload de um arquivo vazio.");
        }
    }

    private void validarArquivoExistente(String caminho, String bucketNome) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(caminho)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentoEscolarNaoEncontradoException("Arquivo não encontrado");
        }

    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".pdf")) {
            return "application/pdf";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else {
            return "application/octet-stream";
        }
    }

    private String extrairObjectNameDaUrl(String url, String bucket) {
        String base = "/" + bucket + "/";
        int start = url.indexOf(base);
        if (start == -1) {
            throw new DocumentoEscolarException("Não foi possível extrair o caminho do arquivo.");
        }
        start += base.length();
        int end = url.indexOf("?", start);
        if (end == -1) {
            return url.substring(start);
        }
        return url.substring(start, end);
    }
}

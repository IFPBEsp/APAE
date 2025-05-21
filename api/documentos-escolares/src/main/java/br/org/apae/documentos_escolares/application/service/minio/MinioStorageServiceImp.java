package br.org.apae.documentos_escolares.application.service.minio;

import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUpdateRequestDTO;
import br.org.apae.documentos_escolares.api.dto.request.DocumentoEscolarUploadRequestDTO;
import br.org.apae.documentos_escolares.api.dto.response.DocumentoEscolarResponseDTO;
import br.org.apae.documentos_escolares.api.dto.response.UrlPreAssinadaResponseDTO;
import br.org.apae.documentos_escolares.domain.exception.ArquivoVazioException;
import br.org.apae.documentos_escolares.domain.exception.BucketNaoExisteException;
import br.org.apae.documentos_escolares.domain.exception.DocumentoEscolarException;
import br.org.apae.documentos_escolares.domain.exception.UploadDocumentoException;
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
        validarBucket(dto.pacienteId().toString());
        validarArquivo(arquivo);

        String caminho = PASTA_DOCUMENTOS + "/" + dto.ano() + "/" + arquivo.getOriginalFilename();

        try (InputStream arquivoInputStream = arquivo.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.pacienteId().toString())
                            .object(caminho)
                            .stream(arquivoInputStream, arquivo.getSize(), -1)
                            .contentType(arquivo.getContentType() != null ? arquivo.getContentType() : "application/octet-stream")
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

    private DocumentoEscolarResponseDTO listarDocumentosComPrefixo(UUID pacienteId, String prefix) {
        String bucket = pacienteId.toString();
        List<UrlPreAssinadaResponseDTO> urls = new ArrayList<>();

        try {
            Iterable<Result<Item>> objetos = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucket)
                            .prefix(prefix)
                            .build()
            );

            for (Result<Item> result : objetos) {
                Item item = result.get();
                String fileName = item.objectName().substring(item.objectName().lastIndexOf('/') + 1);

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
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro ao listar documentos: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao listar documentos: " + e.getMessage());
        }

        return new DocumentoEscolarResponseDTO(pacienteId, urls);
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolares(UUID pacienteId) {
        String prefix = PASTA_DOCUMENTOS + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }

    @Override
    public DocumentoEscolarResponseDTO listarDocumentosEscolaresAno(UUID pacienteId, Integer ano) {
        String prefix = PASTA_DOCUMENTOS + "/" + ano + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }

    @Override
    public DocumentoEscolarResponseDTO historicoDocumentosEscolares(UUID pacienteId) {
        String prefix = PASTA_DOCUMENTOS + "/";
        return listarDocumentosComPrefixo(pacienteId, prefix);
    }


    @Override
    public byte[] visualizarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        String bucket = pacienteId.toString();
        String caminhoArquivo = PASTA_DOCUMENTOS + "/" + nomeArquivo;

        try (InputStream is = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(caminhoArquivo)
                        .build())) {
            if (is == null) {
                throw new DocumentoEscolarException("Arquivo não encontrado: " + nomeArquivo);
            }
            return is.readAllBytes();
        } catch (Error | IOException e) {
            throw new DocumentoEscolarException("Erro ao ler arquivo: " + e.getMessage());
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro no serviço de armazenamento: " + e.getMessage());
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public void atualizarDocumento(DocumentoEscolarUpdateRequestDTO dto, MultipartFile arquivo) {
//        deletarDocumentoEscolar(dto.pacienteId(), dto.documentoNome());
//        DocumentoEscolarUploadRequestDTO uploadDto = new DocumentoEscolarUploadRequestDTO(dto.pacienteId(), dto.ano());
//        salvarArquivo(uploadDto, arquivo);
//    }

    @Override
    public void atualizarDocumento(DocumentoEscolarUpdateRequestDTO dto, MultipartFile arquivo) {
        validarBucket(dto.pacienteId().toString());
        validarArquivo(arquivo);

        deletarDocumentoEscolar(dto.pacienteId(), dto.documentoNome());

        String caminho = PASTA_DOCUMENTOS + "/" + dto.ano() + "/" + dto.novoNome();

        try (InputStream arquivoInputStream = arquivo.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.pacienteId().toString())
                            .object(caminho)
                            .stream(arquivoInputStream, arquivo.getSize(), -1)
                            .contentType(arquivo.getContentType() != null ? arquivo.getContentType() : "application/octet-stream")
                            .build()
            );
        } catch (IOException e) {
            throw new DocumentoEscolarException("Erro ao processar o arquivo: " + e.getMessage());
        } catch (MinioException e) {
            throw new UploadDocumentoException("Erro ao fazer upload do documento: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao atualizar arquivo: " + e.getMessage());
        }
    }



    @Override
    public void deletarDocumentoEscolar(UUID pacienteId, String nomeArquivo) {
        String bucket = pacienteId.toString();
        String caminhoArquivo = PASTA_DOCUMENTOS + "/" + nomeArquivo;

        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucket)
                            .object(caminhoArquivo)
                            .build()
            );
        } catch (MinioException e) {
            throw new DocumentoEscolarException("Erro ao deletar arquivo: " + e.getMessage());
        } catch (Exception e) {
            throw new DocumentoEscolarException("Erro inesperado ao deletar arquivo: " + e.getMessage());
        }
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

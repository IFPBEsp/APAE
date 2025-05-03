package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.exception.DocumentoStorageException;
import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import br.org.apae.documentos_digitalizados.domain.TipoPaciente;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Override
    public void criarBucket(String bucketNome, TipoPaciente tipoPaciente) {
        try {
            if (!existeBucket(bucketNome)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketNome).build());

                switch (tipoPaciente) {
                    case PACIENTE:
                        criarSubBucket(bucketNome, "documentos-medico");
                        criarSubBucket(bucketNome, "documentos-pessoal");
                        break;
                    case ALUNO:
                        criarSubBucket(bucketNome, "documentos-escolar");
                        criarSubBucket(bucketNome, "documentos-pessoal");
                        break;
                    case AMBOS:
                        criarSubBucket(bucketNome, "documentos-escolar");
                        criarSubBucket(bucketNome, "documentos-medico");
                        criarSubBucket(bucketNome, "documentos-pessoal");
                        break;
                }
            }
        } catch (Exception e) {
            throw new DocumentoStorageException("Erro ao criar bucket\n" + e.getMessage());
        }
    }

    @Override
    public void uploadDocumento(String nomeBucket, TipoDocumento tipoDocumento, String documentoNome, MultipartFile file) {
        validarArquivo(file);

        String subBucket = determinarSubBucket(tipoDocumento);

        try (InputStream inputStream = file.getInputStream()) {
            String caminhoCompleto = subBucket + documentoNome;
            salvarDocumento(nomeBucket, caminhoCompleto, inputStream, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new DocumentoStorageException("Falha ao ler arquivo\n" + e);
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha no upload para MinIO\n" + e);
        }
    }

    @Override
    public InputStream downloadDocumento(String bucketNome, String documentoCaminho) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(documentoCaminho)
                            .build()
            );
        }  catch (Exception e) {
            throw new DocumentoStorageException("Falha no download do documento\n" + e);
        }
    }

    @Override
    public StatObjectResponse metadadoDocumento(String bucketNome, String documentoCaminho) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketNome)
                            .object(documentoCaminho)
                            .build()
            );
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha no busca do metadado do documento\n" + e);
        }
    }

    @Override
    public List<String> listarDocumentos(String bucketNome, String subBucket) {
        List<String> documentos = new ArrayList<>();

        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketNome)
                        .prefix(subBucket)
                        .recursive(false)
                        .build()
        );

        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                if (!item.isDir()) {
                    documentos.add(item.objectName());
                }
            }
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha ao listar documentos\n" + e);
        }

        return documentos;
    }

    @Override
    public void atualizarDocumento(String bucketNome, TipoDocumento tipoDocumento, String documentoNome, MultipartFile file) {
        uploadDocumento(bucketNome, tipoDocumento, documentoNome, file);
    }

    private void validarArquivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DocumentoStorageException("Arquivo não pode ser nulo ou vazio");
        }

        if (file.getContentType() == null) {
            throw new DocumentoStorageException("Tipo de conteúdo não identificado");
        }
    }

    private String determinarSubBucket(TipoDocumento tipo) {
        return switch (tipo) {
            case PESSOAL -> "documentos-pessoal/";
            case MEDICO -> "documentos-medico/";
            case ESCOLAR -> "documentos-escolar/";
        };
    }

    private void salvarDocumento(String bucket, String documentoNome, InputStream inputStream, String contentType, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(documentoNome)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha no upload para MinIO\n" + e);
        }
    }

    private boolean existeBucket(String bucketNome) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketNome).build());
        } catch (Exception e) {
            throw new DocumentoStorageException("Erro ao buscar bucket\n" + e.getMessage());
        }
    }

    private void criarSubBucket(String bucket, String subBucket) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(subBucket)
                            .stream(new ByteArrayInputStream(new byte[0]), 0, -1)
                            .build()
            );
        } catch (Exception e){
            throw new DocumentoStorageException("Falha ao criar arquivo\n" + e);
        }
    }
}

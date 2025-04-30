package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.application.exception.DocumentoStorageException;
import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class MinioStorageServiceImp implements MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.buckets.pessoal}")
    private String bucketDocumentoPessoal;

    @Value("${minio.buckets.medico}")
    private String bucketDocumentoMedico;

    @Value("${minio.buckets.escolar}")
    private String bucketDocumentoEscolar;


    @Override
    public void uploadDocumento(TipoDocumento tipo, String documentoNome, MultipartFile file) {
        validarArquivo(file);

        String bucket = determinarBucket(tipo);

        try (InputStream inputStream = file.getInputStream()) {
            salvarDocumento(bucket, documentoNome, inputStream, file.getContentType(), file.getSize());
        } catch (IOException e) {
            throw new DocumentoStorageException("Falha ao ler arquivo\n" + e);
        }
    }

    public Resource downloadDocumento(TipoDocumento tipo, String documentoNome) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(determinarBucket(tipo))
                            .object(documentoNome)
                            .build()
            );

            return new InputStreamResource(stream) {
                @Override
                public String getFilename() {
                    return documentoNome;
                }
            };
        } catch (Exception e) {
            throw new DocumentoStorageException("Erro ao baixar documento\n" + e);
        }
    }

    private void validarArquivo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DocumentoStorageException("Arquivo não pode ser nulo ou vazio");
        }

        if (file.getContentType() == null) {
            throw new DocumentoStorageException("Tipo de conteúdo não identificado");
        }
    }

    private String determinarBucket(TipoDocumento tipo) {
        return switch (tipo) {
            case PESSOAL -> bucketDocumentoPessoal;
            case MEDICO -> bucketDocumentoMedico;
            case ESCOLAR -> bucketDocumentoEscolar;
        };
    }

    private void salvarDocumento(String bucket, String objectName, InputStream inputStream, String contentType, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(inputStream, size, -1)
                            .contentType(contentType)
                            .build());
        } catch (Exception e) {
            throw new DocumentoStorageException("Falha no upload para MinIO\n" + e);
        }
    }
}

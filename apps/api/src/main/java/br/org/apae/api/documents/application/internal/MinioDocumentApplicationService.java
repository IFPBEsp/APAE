package br.org.apae.api.documents.application.internal;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.builders.DocumentReferenceBuilder;
import br.org.apae.api.documents.domain.mappers.DocumentMetadataMapper;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.exceptions.ErrorResponseException;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;
import br.org.apae.api.documents.interfaces.exceptions.InternalException;
import br.org.apae.api.documents.interfaces.exceptions.InvalidResponseException;
import br.org.apae.api.documents.interfaces.exceptions.ServerException;
import br.org.apae.api.documents.interfaces.exceptions.XmlParserException;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import io.minio.messages.Item;

@Service
public class MinioDocumentApplicationService implements DocumentApplicationService {
    private final MinioClient client;
    private final MinioClient publicClient;

    public MinioDocumentApplicationService(
            MinioClient client,
            @Qualifier("minioPublicClient") MinioClient publicClient) {
        this.client = client;
        this.publicClient = publicClient;
    }

    private static RuntimeException translateMinioException(MinioException exception) {
        if (exception instanceof io.minio.errors.InvalidResponseException)
            return new InvalidResponseException();
        if (exception instanceof io.minio.errors.ServerException)
            return new ServerException();
        if (exception instanceof io.minio.errors.ErrorResponseException)
            return new ErrorResponseException();
        if (exception instanceof io.minio.errors.XmlParserException)
            return new XmlParserException();
        return new InternalException();
    }

    private void makeBucketIfNotExists(String bucket)
            throws MinioException, InvalidKeyException, IOException, NoSuchAlgorithmException {
        boolean found = client.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucket)
                        .build());

        if (!found) {
            client.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucket)
                            .build());
        }
    }

    @Override
    public DocumentDTO putDocument(PutDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException, InvalidResponseException,
            NoSuchAlgorithmException {
        UUID id = UUID.randomUUID();
        String name = DocumentReferenceBuilder.buildDocumentName(
                dto.category(),
                dto.type(),
                dto.year(),
                id);
        Map<String, String> metadata = DocumentMetadataMapper.from(
                id,
                dto.category(),
                dto.type(),
                dto.owner(),
                dto.year());

        try {
            makeBucketIfNotExists(dto.owner());

            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.owner())
                            .stream(dto.stream(), dto.stream().available(), -1)
                            .contentType(dto.contentType())
                            .userMetadata(metadata)
                            .object(name)
                            .build());

            return new DocumentDTO(
                    id, name,
                    dto.category(),
                    dto.type(),
                    dto.owner(),
                    dto.year());
        } catch (MinioException exception) {
            throw translateMinioException(exception);
        }
    }

    @Override
    public InputStream getDocument(GetDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException, InvalidResponseException,
            NoSuchAlgorithmException {
        try {
            return client.getObject(
                    GetObjectArgs.builder()
                            .bucket(dto.owner())
                            .object(dto.name())
                            .build());
        } catch (MinioException exception) {
            throw translateMinioException(exception);
        }
    }

    @Override
    public void removeDocument(RemoveDocumentArgsDTO dto)
            throws InsufficientDataException, IOException,
            InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException {
        try {
            client.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(dto.owner())
                            .object(dto.name())
                            .build());
        } catch (MinioException exception) {
            throw translateMinioException(exception);
        }
    }

    private static Iterable<DocumentDTO> mapDocuments(Iterable<Result<Item>> results)
            throws InsufficientDataException, IOException,
            InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException {
        List<DocumentDTO> documents = new ArrayList<>();

        try {
            for (Result<Item> result : results) {
                Item item = result.get();
                documents.add(
                        DocumentMetadataMapper.from(
                                item.userMetadata()));
            }
        } catch (MinioException e) {
            throw translateMinioException(e);
        }

        return documents;

    }

    @Override
    public Iterable<DocumentDTO> listDocuments(ListDocumentsArgsDTO dto)
            throws InsufficientDataException, IOException,
            InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException {
        try {
            boolean bucketExists = client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(dto.owner())
                            .build());
            if (!bucketExists) {
                return List.of();
            }
        } catch (MinioException e) {
            throw translateMinioException(e);
        }

        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
                .bucket(dto.owner())
                .includeUserMetadata(true)
                .recursive(true);

        Optional.ofNullable(dto.category())
                .ifPresent(category -> builder.prefix(
                        DocumentReferenceBuilder.buildDocumentPrefix(
                                dto.category(),
                                dto.year(),
                                dto.type())));

        return mapDocuments(
                client.listObjects(
                        builder.build()));
    }

    @Override
    public String getPresignedDocumentUrl(GetPresignedDocumentUrlArgsDTO dto)
            throws InsufficientDataException, IOException,
            InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException {
        try {
            return publicClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(dto.owner())
                            .object(dto.name())
                            .expiry(dto.expiry())
                            .build());
        } catch (MinioException e) {
            throw translateMinioException(e);
        }
    }
}

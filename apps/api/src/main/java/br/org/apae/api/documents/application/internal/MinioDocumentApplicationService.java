package br.org.apae.api.documents.application.internal;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.UUID;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import br.org.apae.api.documents.application.interfaces.DocumentApplicationService;
import br.org.apae.api.documents.domain.builders.DocumentReferenceBuilder;
import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.DocumentWriteResponseDTO;
import br.org.apae.api.documents.interfaces.dto.GetDocumentArgsDTO;
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
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

@Service
public class MinioDocumentApplicationService implements DocumentApplicationService {
    private final MinioClient client;

    public MinioDocumentApplicationService(MinioClient client) {
        this.client = client;
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
    public DocumentWriteResponseDTO putDocument(PutDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException, InvalidResponseException,
            NoSuchAlgorithmException {
        UUID id = UUID.randomUUID();
        String name = DocumentReferenceBuilder.buildDocumentName(
                dto.category(),
                dto.type(),
                dto.year(),
                id);

        try {
            makeBucketIfNotExists(dto.owner());

            client.putObject(
                    PutObjectArgs.builder()
                            .bucket(dto.owner())
                            .stream(dto.stream(), dto.stream().available(), -1)
                            .contentType(dto.contentType())
                            .object(name)
                            .build());

            return new DocumentWriteResponseDTO(
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

    private static Iterable<DocumentDTO> mapDocuments(Iterable<Result<Item>> results, ListDocumentsArgsDTO dto)
            throws InsufficientDataException, IOException,
            InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException {
        List<DocumentDTO> documents = new ArrayList<>();

        try {
            for (Result<Item> result : results) {
                String name = result.get()
                        .objectName();
                documents.add(
                        new DocumentDTO(name,
                                dto.category(),
                                dto.type(),
                                dto.owner(),
                                dto.year()));
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
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
                .bucket(dto.owner())
                .recursive(true);

        Optional.ofNullable(dto.category())
                .ifPresent((category) -> builder.prefix(
                        DocumentReferenceBuilder.buildDocumentPrefix(
                                dto.category(),
                                dto.year(),
                                dto.type())));

        return mapDocuments(
                client.listObjects(
                        builder.build()),
                dto);
    }
}

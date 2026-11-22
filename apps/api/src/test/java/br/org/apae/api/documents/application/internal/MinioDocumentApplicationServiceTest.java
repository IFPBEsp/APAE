package br.org.apae.api.documents.application.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import br.org.apae.api.documents.domain.enums.DocumentCategory;
import br.org.apae.api.documents.domain.enums.DocumentType;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@ExtendWith(MockitoExtension.class)
class MinioDocumentApplicationServiceTest {

    @Mock
    private MinioClient client;

    @Mock
    private MinioClient publicClient;

    private MinioDocumentApplicationService service;

    @BeforeEach
    void setUp() {
        service = new MinioDocumentApplicationService(client, publicClient);
    }

    @Test
    void shouldUseDtoSizeInPutObjectArgs() throws Exception {
        long size = 12_345L;

        PutDocumentArgsDTO dto = PutDocumentArgsDTO.builder()
                .stream(new ByteArrayInputStream(new byte[(int) size]))
                .size(size)
                .category(DocumentCategory.MEDICAL)
                .type(DocumentType.EXAMINATION)
                .contentType("application/pdf")
                .owner(UUID.randomUUID().toString())
                .build();

        when(client.bucketExists(any(BucketExistsArgs.class))).thenReturn(true);

        service.putDocument(dto);

        ArgumentCaptor<PutObjectArgs> captor = ArgumentCaptor.forClass(PutObjectArgs.class);
        verify(client).putObject(captor.capture());

        assertEquals(size, captor.getValue().objectSize());
    }
}

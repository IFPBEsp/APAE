package br.org.apae.documentos_medicos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.org.apae.documentos_medicos.application.MedicalDocumentServiceImpl;
import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@ExtendWith(MockitoExtension.class)
public class DeleteDocumentTest {
    @Mock
    private MinioStorage minioStorage;

    @Mock
    private StorageClient storageClient;

    @InjectMocks
    private MedicalDocumentServiceImpl service;

    @Test
    void deleteDocumentShouldCallDeleteFile() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        String path = "documentos-medicos/2025/EXAME/exame1.png";

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.doNothing().when(minioStorage).deleteFile(patientId, path);

        Assertions.assertDoesNotThrow(() -> service.deleteDocument(patientId, path));

        Mockito.verify(minioStorage, Mockito.times(1)).deleteFile(patientId, path);
    }

    @Test
    void deleteDocumentShouldThrowExceptionWhenBucketNotFound() {
        String patientId = "invalid-bucket-id";
        String path = "documentos-medicos/2025/EXAME/exame1.png";

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(false);

        Assertions.assertThrows(BucketNotFoundException.class,
            () -> service.deleteDocument(patientId, path),
            "Bucket não existe: " + patientId
        );
    }
}

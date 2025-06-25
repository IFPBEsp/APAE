package br.org.apae.documentos_medicos;

import static org.mockito.ArgumentMatchers.nullable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.org.apae.documentos_medicos.application.MedicalDocumentServiceImpl;
import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@ExtendWith(MockitoExtension.class)
public class ViewDocumentTest {
    @Mock
    private MinioStorage minioStorage;

    @Mock
    private StorageClient storageClient;

    @InjectMocks
    private MedicalDocumentServiceImpl service;

    @Test
    void viewMedicalDocumentShouldReturnDocumentSuccessfully() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        String path = "documentos-medicos/2025/EXAME/exame1.png";
        byte[] file = "fake file".getBytes();

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.getMedicalDocumentByFileName(patientId, path)).thenReturn(file);

        byte[] result = Assertions.assertDoesNotThrow(() ->service.viewPatientMedicalDocuments(patientId, path));

        Assertions.assertArrayEquals(file, result);
    }

    @Test
    void viewMedicalDocumentShouldThrowWhenBucketDoesNotExist() {
        String patientId = "invalido-bucket-id";
        String path = "documentos-medicos/2025/EXAME/exame1.pdf";

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(false);

        Assertions.assertThrows(
            BucketNotFoundException.class,
            () -> service.viewPatientMedicalDocuments(patientId, path),
            "Bucket não existe: " + patientId
        );
    }

    @Test
    void viewMedicalDocumentShouldThrowWhenDocumentDoesNotExist() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        String path = "documentos-medicos/2025/EXAME/exame1.pdf";

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.getMedicalDocumentByFileName(patientId, path))
        .thenReturn(nullable(byte[].class));

        Assertions.assertThrows(
            DocumentNotFoundException.class,
            () -> service.viewPatientMedicalDocuments(patientId, path),
            "Documento não encontrado."
        );
    }
}

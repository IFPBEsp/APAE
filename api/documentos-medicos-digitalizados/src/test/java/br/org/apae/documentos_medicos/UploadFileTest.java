package br.org.apae.documentos_medicos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import br.org.apae.documentos_medicos.api.dto.requests.MedicalDocumentUploadDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentServiceImpl;
import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.FileIsEmptyException;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@ExtendWith(MockitoExtension.class)
public class UploadFileTest {

    @Mock
    private MinioStorage minioStorage;

    @Mock
    private StorageClient storageClient;

    @InjectMocks
    private MedicalDocumentServiceImpl service;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        Mockito.reset(multipartFile, storageClient, minioStorage);
    }

    @Test
    @Order(1)
    void saveFileShouldUploadFileSuccessfully() throws Exception {
        MedicalDocumentUploadDTO dto = new MedicalDocumentUploadDTO(
            "43b4827a-bc4e-41ff-bd72-3205486acee9",
            2024,
            "EXAME"
        );

        byte[] fileBytes = "fake file content".getBytes();

        Mockito.when(storageClient.bucketExists(dto.getPatientId())).thenReturn(true);
        Mockito.when(multipartFile.isEmpty()).thenReturn(false);
        Mockito.when(multipartFile.getSize()).thenReturn(1L);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn("arquivo.png");
        Mockito.when(multipartFile.getBytes()).thenReturn(fileBytes);

        assertDoesNotThrow(() -> service.saveFile(dto, multipartFile));


        String expectPath = "documentos-medicos/%d/%s/%s".formatted(
            dto.getYear(),
            MedcialDocumentType.valueOf(dto.getDocumentType()).getPrefix(),
            "arquivo.png"
        );

        Mockito.verify(minioStorage).uploadFile(dto.getPatientId(), expectPath, fileBytes);
    }


    @Test
    void saveFileShouldThrowWhenBucketDoesNotExist() {
        MedicalDocumentUploadDTO dto = new MedicalDocumentUploadDTO(
            "bucket-invalido",
            2024,
            "EXAME"
        );
        
        Mockito.when(storageClient.bucketExists(dto.getPatientId())).thenReturn(false);
        
        Assertions.assertThrows(BucketNotFoundException.class, () -> {
            service.saveFile(dto, multipartFile);
        });
    }

    @Test
    @Order(2)
    void saveFileShouldThrowWhenFileIsEmpty() throws Exception {
        MedicalDocumentUploadDTO dto = new MedicalDocumentUploadDTO(
            "43b4827a-bc4e-41ff-bd72-3205486acee9",
            2024,
            "EXAME"
        );
        
        Mockito.when(storageClient.bucketExists(dto.getPatientId())).thenReturn(true);
        Mockito.when(multipartFile.isEmpty()).thenReturn(true);

        Assertions.assertThrows(FileIsEmptyException.class, () -> {
            service.saveFile(dto, multipartFile);
        });

        Mockito.verify(minioStorage, Mockito.never()).uploadFile(Mockito.anyString(), Mockito.anyString(), Mockito.any(byte[].class));
    }
}

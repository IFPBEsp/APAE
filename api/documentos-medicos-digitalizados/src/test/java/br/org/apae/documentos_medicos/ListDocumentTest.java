package br.org.apae.documentos_medicos;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.Assert;

import br.org.apae.documentos_medicos.api.dto.responses.MedicalDocumentResponseDTO;
import br.org.apae.documentos_medicos.application.MedicalDocumentServiceImpl;
import br.org.apae.documentos_medicos.application.exceptions.BucketNotFoundException;
import br.org.apae.documentos_medicos.application.exceptions.DocumentNotFoundException;
import br.org.apae.documentos_medicos.domain.models.MedcialDocumentType;
import br.org.apae.documentos_medicos.domain.ports.storage.MinioStorage;
import br.org.apae.documentos_medicos.infrastructure.client.StorageClient;

@ExtendWith(MockitoExtension.class)
public class ListDocumentTest {
    @Mock
    private MinioStorage minioStorage;

    @Mock
    private StorageClient storageClient;

    @InjectMocks
    private MedicalDocumentServiceImpl service;

    private static final String FOLDER_NAME = "documentos-medicos";

    @Test
    void listMedicalDocumentShouldReturnDocumentsSuccessfully() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        Integer year = 2024;
        String prefix = FOLDER_NAME + "/" + year + "/";

        List<String> objectNames = List.of("documentos-medicos/2024/EXAME/exame1.png");
        List<String> presignedUrls = List.of("http://fake-url.com/doc");

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.listObject(patientId, prefix)).thenReturn(objectNames);
        Mockito.when(minioStorage.generatePresignedUrls(
            eq(patientId), 
            eq(objectNames), 
            anyInt()))
        .thenReturn(presignedUrls);

        MedicalDocumentResponseDTO result = assertDoesNotThrow(() -> service.listMedicalDocument(patientId, year));

        Assertions.assertEquals(patientId, result.patientId());
        Assertions.assertEquals(1, result.urls().size());
        Assertions.assertEquals("documentos-medicos/2024/EXAME/exame1.png", result.urls().get(0).fileName());
        Assertions.assertEquals("http://fake-url.com/doc", result.urls().get(0).link());
    }

    @Test
    void listMedicalDocumentShouldThrowWhenBucketDoesNotExist() {
        String patientId = "invalido-bucket-id";
        Integer year = 2024;

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(false);
        
        Assertions.assertThrows(
            BucketNotFoundException.class,
            () -> service.listMedicalDocument(patientId, year),
            "Bucket não existe: " + patientId
        );
    }

    @Test
    void listMedicalDocumentShouldThrowWhenDocumentNotFoundByYear() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        Integer year = 2050;
        String prefix = FOLDER_NAME + "/" + year + "/";

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.listObject(patientId, prefix)).thenReturn(List.of());

        Exception exception = Assertions.assertThrows(
            DocumentNotFoundException.class,
            () -> service.listMedicalDocument(patientId, year)
        );

        Assert.isTrue(
            exception.getMessage().contains(
                "Nenhum documento encontrado para o paciente " + patientId + " no ano " + year
            ),
            "A mensagem de erro deve indicar que nenhum documento foi encontrado para o ano especificado."
        );
    }

    @Test
    void listMedicalDocumentByYearAndTypeShouldReturnDocumentsSuccessfully() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        Integer year = 2025;
        MedcialDocumentType type = MedcialDocumentType.EXAME;
        String prefix = FOLDER_NAME + "/" + year + "/" + type.getPrefix() + "/";

        List<String> objectNames = List.of("documentos-medicos/2025/EXAME/exame1.png");
        List<String> presignedUrls = List.of("http://fake-url.com/doc");

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.listObject(patientId, prefix)).thenReturn(objectNames);
        Mockito.when(minioStorage.generatePresignedUrls(
            eq(patientId), 
            eq(objectNames), 
            anyInt()))
        .thenReturn(presignedUrls);

        MedicalDocumentResponseDTO result = assertDoesNotThrow(
            () -> service.listMedicalDocumentByType(patientId, year, type)
        );

        Assertions.assertEquals(patientId, result.patientId());
        Assertions.assertEquals(1, result.urls().size());
        Assertions.assertEquals("documentos-medicos/2025/EXAME/exame1.png", result.urls().get(0).fileName());
        Assertions.assertEquals("http://fake-url.com/doc", result.urls().get(0).link());
    }

    @Test
    void getDocumentByTypeShouldReturnDocumentsSuccessfully() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        MedcialDocumentType type = MedcialDocumentType.EXAME;
        String prefix = FOLDER_NAME + "/";

        List<String> objectNames = List.of(
            "documentos-medicos/2025/EXAME/exame1.png",
            "documentos-medicos/2023/EXAME/exame2.png",
            "documentos-medicos/2025/LAUDO/laudo1.png",
            "documentos-medicos/2024/EXAME/exame3.png"
        );
        List<String> filteredObjectNames = List.of(
            "documentos-medicos/2025/EXAME/exame1.png",
            "documentos-medicos/2023/EXAME/exame2.png",
            "documentos-medicos/2024/EXAME/exame3.png"
        );
        List<String> presignedUrls = List.of(
            "http://fake-url.com/doc/1",
            "http://fake-url.com/doc/2",
            "http://fake-url.com/doc/3"
        );

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.listObject(patientId, prefix)).thenReturn(objectNames);
        Mockito.when(minioStorage.generatePresignedUrls(
        eq(patientId), 
        anyList(),
        anyInt()))
        .thenReturn(presignedUrls);

        MedicalDocumentResponseDTO result = assertDoesNotThrow(
            () -> service.getDocumentHistoryByType(patientId, type)
        );

        Assertions.assertEquals(patientId, result.patientId());
        Assertions.assertEquals(3, result.urls().size());

        for (int i = 0; i < result.urls().size(); i++) {
            Assertions.assertEquals(filteredObjectNames.get(i), result.urls().get(i).fileName());
            Assertions.assertEquals(presignedUrls.get(i), result.urls().get(i).link());
        }
    }

    @Test
    void getDocumentShouldThrowWhenDocumentNotFoundByType() {
        String patientId = "43b4827a-bc4e-41ff-bd72-3205486acee9";
        MedcialDocumentType type = MedcialDocumentType.EXAME;
        String prefix = FOLDER_NAME + "/";
        String messsageError = "Nenhum documento foi encontrado para o paciente %s do tipo %s".formatted(patientId, type);
        
        List<String> objectNames = List.of(
            "documentos-medicos/2025/LAUDO/laudo1.png",
            "documentos-medicos/2023/LAUDO/laudo2.png"
        );

        Mockito.when(storageClient.bucketExists(patientId)).thenReturn(true);
        Mockito.when(minioStorage.listObject(patientId, prefix)).thenReturn(objectNames);

        Exception exception = Assertions.assertThrows(
            DocumentNotFoundException.class,
            () -> service.getDocumentHistoryByType(patientId, type)
        );

        Assertions.assertTrue(
            exception.getMessage().contains(messsageError),
            "A mensagem de erro deve indicar que nenhum documento foi encontrado para o tipo especificado."
        );
    }
}

package br.org.apae.documentos_medicos.domain.ports.storage;

import java.util.List;

public interface MinioStorage {
    void uploadFile(String bucket, String path, byte[] file);
    List<String> listObject(String bucket, String prefix);
    byte[] getMedicalDocumentByFileName(String bucket, String objectName);
    List<String> generatePresignedUrls(String bucket, List<String> objectNames, int expirationTimeInHours);
}

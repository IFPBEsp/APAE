package br.org.apae.documentos_pessoais_digitalizados.domain.ports.storage;

import java.util.List;

public interface MinioStorage {
    void deleteFile(String bucket, String objectName);

    List<String> generatePresignedUrls(String bucket, List<String> objectNames, int expirationTimeInHours);

    List<String> listObject(String bucket, String prefix);

    void uploadFile(String bucket, String path, byte[] file);

    byte[] getPersonalDocumentByFileName(String bucket, String objectName);

}

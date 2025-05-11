package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;

public interface StorageService {
    String uploadDocument(PersonalDocumentFileReqDTO personalDocument, String bucketName);
    byte[] findDocumentByFileName(String fileName, String bucketName);
    void deleteFile(String fileName, String bucketName);
    String updateFile(PersonalDocumentFileReqDTO personalDocument, String bucketName, String pathOfOldFile);
}

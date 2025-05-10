package br.org.apae.documentos_pessoais_digitalizados.application.service;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;

import java.io.FileNotFoundException;

public interface StorageService {
    String uploadDocuments(PersonalDocumentFileReqDTO personalDocument, String bucketName);
    byte[] findDocumentByFileName(String fileName, String bucketName) throws FileNotFoundException;
}

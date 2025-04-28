package br.org.apae.documentos_digitalizados.application.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface MinioStorageService {
    void uploadLaudo(UUID documentId, MultipartFile file) throws Exception;
    void uploadEncaminhamento(UUID documentId, MultipartFile file) throws Exception;
    InputStream downloadLaudo(UUID documentId) throws Exception ;
    InputStream downloadEncaminhamento(UUID documentId) throws Exception ;
}

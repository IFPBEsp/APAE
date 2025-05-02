package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioStorageService {
    void criarBucket(String bucketNome);
    boolean existeBucket(String bucketNome);
    String listarDocumentos(String bucketNome, String subBucket);
    void uploadDocumento(TipoDocumento tipo, String documentoNome, MultipartFile file);
    InputStream downloadDocumento(TipoDocumento tipo, String documentoNome);
    void atualizarDocumento(String bucketNome, String documentoNome, MultipartFile file);
}

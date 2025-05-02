package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import br.org.apae.documentos_digitalizados.domain.TipoPaciente;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioStorageService {
    void criarBucket(String bucketNome, TipoPaciente tipoPaciente);
    void uploadDocumento(String nomeBucket, TipoDocumento tipoDocumento, String documentoNome, MultipartFile file);
    InputStream downloadDocumento(String bucketNome, String documentoCaminho);
    List<String> listarDocumentos(String bucketNome, String subBucket);
    void atualizarDocumento(String bucketNome,TipoDocumento tipoDocumento, String documentoNome, MultipartFile file);
}

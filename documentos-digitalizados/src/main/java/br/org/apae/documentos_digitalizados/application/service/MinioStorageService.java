package br.org.apae.documentos_digitalizados.application.service;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MinioStorageService {

    void uploadDocumento(TipoDocumento tipo, String documentoNome, MultipartFile file);
    Resource downloadDocumento(TipoDocumento tipo, String documentoNome);
}

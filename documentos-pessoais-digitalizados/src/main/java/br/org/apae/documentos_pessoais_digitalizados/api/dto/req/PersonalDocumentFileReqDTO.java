package br.org.apae.documentos_pessoais_digitalizados.api.dto.req;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import org.springframework.web.multipart.MultipartFile;

public record PersonalDocumentFileReqDTO(PersonalDocumentType personalDocumentType, MultipartFile file) {
}

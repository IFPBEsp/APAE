package br.org.apae.documentos_pessoais_digitalizados.api.dtos.req;

import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocumentType;
import org.springframework.web.multipart.MultipartFile;

public record PersonalDocumentFileReqDTO(PersonalDocumentType personalDocumentType, MultipartFile document) {
}

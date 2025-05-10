package br.org.apae.documentos_pessoais_digitalizados.api.dto.req;

import java.util.List;

public record PersonalDocumentReqDTO(List<PersonalDocumentFileReqDTO> documents) {
}

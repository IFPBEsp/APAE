package br.org.apae.documentos_pessoais_digitalizados.api.dto.res;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;

public record PersonalDocumentResUrlDTO(String url, PersonalDocumentType documentType) {
}

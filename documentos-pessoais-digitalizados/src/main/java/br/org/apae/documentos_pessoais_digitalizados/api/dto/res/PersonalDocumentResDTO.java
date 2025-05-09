package br.org.apae.documentos_pessoais_digitalizados.api.dto.res;

import java.util.List;

public record PersonalDocumentResDTO(List<PersonalDocumentURL> documentURLS) {
}
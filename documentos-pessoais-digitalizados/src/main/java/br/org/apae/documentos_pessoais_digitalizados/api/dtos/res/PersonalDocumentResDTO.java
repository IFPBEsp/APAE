package br.org.apae.documentos_pessoais_digitalizados.api.dtos.res;

import java.util.List;

public record PersonalDocumentResDTO(List<PersonalDocumentURL> documentURLS) {
}
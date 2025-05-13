package br.org.apae.documentos_digitalizados.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoDocumento {
    PESSOAL("pessoal"),
    MEDICO("medico"),
    ESCOLAR("escolar");

    private final String tipo;
}

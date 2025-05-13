package br.org.apae.documentos_digitalizados.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoPaciente {
    ALUNO("aluno"),
    PACIENTE("paciente"),
    AMBOS("ambos");

    private final String tipoPaciente;
}

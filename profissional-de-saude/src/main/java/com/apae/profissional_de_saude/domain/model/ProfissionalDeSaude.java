package com.apae.profissional_de_saude.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ProfissionalDeSaude {
    private Long id;
    private String nome;
    private String especialidade;
    private String contato;

    private List<Long> relatorioIds;
    private List<Long> grupoIds;
}

package br.org.apae.profissional_da_saude.api.dto;

import br.org.apae.profissional_da_saude.domain.model.Endereco;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProfissionalSaudeResponseDTO {

    private UUID id;
    private String areaDaSaude;
    private String telefone;
    private String docProfissional;
    private String email;
    private String nome;
    private String rg;
    private boolean ativo;

    private Endereco endereco;
}

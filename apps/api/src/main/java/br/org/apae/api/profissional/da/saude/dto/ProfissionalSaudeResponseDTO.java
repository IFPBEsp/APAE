package br.org.apae.api.profissional.da.saude.dto;

import br.org.apae.api.common.model.Endereco;
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
    private  String rg;

    private Endereco endereco;
}
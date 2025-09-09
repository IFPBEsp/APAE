package br.org.apae.profissional_da_saude.api.dto;

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
    private  String estado;
    private  String cidade;
    private  String bairro;
    private  String rua;
    private  String numero;
    private  String cep;
    private  String complemento;

}

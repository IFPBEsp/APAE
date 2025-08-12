package br.org.apae.profissional_da_saude.api.dto;

import lombok.Builder;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Builder
public class PacienteResposeDto {
    private UUID id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dateNascimento;
    private String cpf;
    private String rg;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private LocalDateTime dataCriacao;
}

package br.org.apae.profissional_da_saude.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "profissionais_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfissionalSaudeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String areaDaSaude;
    private String telefone;
    private String docProfissional;
    private String email;
    private String nome;
// Inserção de novas entidades
    private  String rg;
    private  String estado;
    private  String cidade;
    private  String bairro;
    private  String rua;
    private  String numero;
    private  String cep;
    private  String complemento;
}

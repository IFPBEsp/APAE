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
    private String rg;
    
    @Column(name = "ativo", nullable = false)
    private boolean ativo;

    @Embedded
    private EnderecoEntity endereco;

}

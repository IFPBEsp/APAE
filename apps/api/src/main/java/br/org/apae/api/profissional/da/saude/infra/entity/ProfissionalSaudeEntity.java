package br.org.apae.api.profissional.da.saude.infra.entity;

import br.org.apae.api.common.entity.EnderecoEntity;
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
    private  String rg;

    @Embedded
    private EnderecoEntity endereco;

}
package br.org.apae.profissional_da_saude.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "profissionais_saude")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = { "disponibilidades" })
@ToString(exclude = { "disponibilidades" })
public class ProfissionalSaudeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String areaDaSaude;
    private String telefone;
    private String docProfissional;
    private String email;
    private String nome;

    @OneToMany(mappedBy = "profissional", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DisponibilidadeEntity> disponibilidades = new ArrayList<>();

    private String rg;

    @Embedded
    private EnderecoEntity endereco;

    public void addDisponibilidade(DisponibilidadeEntity disponibilidade) {
        disponibilidade.setProfissional(this);
        this.disponibilidades.add(disponibilidade);
    }

    public void removeDisponibilidade(DisponibilidadeEntity disponibilidade) {
        disponibilidade.setProfissional(null);
        this.disponibilidades.remove(disponibilidade);
    }
}

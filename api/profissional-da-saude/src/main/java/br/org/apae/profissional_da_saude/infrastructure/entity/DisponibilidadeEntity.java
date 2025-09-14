package br.org.apae.profissional_da_saude.infrastructure.entity;

import br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana;
import br.org.apae.profissional_da_saude.domain.model.enums.Turno;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(
        name = "disponibilidades",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "disponibilidade_profissional_dia_turno",
                        columnNames = {"fk_profissional_id", "dia_semana", "turno"}
                )
        }
)
@Data
public class DisponibilidadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    @NotNull(message = "Dia da semana é obrigatório")
    private DiaSemana diaSemana;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false)
    @NotNull(message = "Turno é obrigatório")
    private Turno turno;

    @Column(name = "fk_profissional_id", nullable = false)
    private UUID fkProfissionalId;

    public DisponibilidadeEntity() {}

    public DisponibilidadeEntity(DiaSemana diaSemana, Turno turno, UUID fkProfissionalId) {
        this.diaSemana = diaSemana;
        this.turno = turno;
        this.fkProfissionalId = fkProfissionalId;
    }
}
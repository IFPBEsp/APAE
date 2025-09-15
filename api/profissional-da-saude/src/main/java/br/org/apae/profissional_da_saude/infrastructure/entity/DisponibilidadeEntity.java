package br.org.apae.profissional_da_saude.infrastructure.entity;

import br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana;
import br.org.apae.profissional_da_saude.domain.model.enums.Turno;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "disponibilidades", uniqueConstraints = {
                @UniqueConstraint(name = "disponibilidade_profissional_dia_turno", columnNames = { "fk_profissional_id",
                                "dia_semana", "turno" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadeEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Enumerated(EnumType.STRING)
        @Column(name = "dia_semana", nullable = false)
        private DiaSemana diaSemana;

        @Enumerated(EnumType.STRING)
        @Column(name = "turno", nullable = false)
        private Turno turno;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "fk_profissional_id", nullable = false)
        private ProfissionalSaudeEntity profissional;

        public DisponibilidadeEntity(DiaSemana diaSemana, Turno turno, ProfissionalSaudeEntity profissional) {
                this.diaSemana = diaSemana;
                this.turno = turno;
                this.profissional = profissional;
        }
}

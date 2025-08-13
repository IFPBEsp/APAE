package br.org.apae.profissional_da_saude.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private PacienteEntity paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_saude_id", nullable = false)
    private ProfissionalSaudeEntity profissionalDaSaude;

    private Integer frequenciaDias;

    private LocalDate proximaConsulta;

    private LocalTime horaProximaConsulta;

    private Boolean confirmado;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

}

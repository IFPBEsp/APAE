package br.org.apae.profissional_da_saude.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "agendamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgendamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "paciente_id", nullable = false)
    private UUID idPaciente;
    @Column(name = "area_da_saude_id", nullable = false)
    private UUID idAreaDaSaude;
    @Column(name = "frequencia_dias", nullable = false)
    private Integer frequenciaDias;
    @Column(name = "proxima_consulta", nullable = false)
    private LocalDate proximaConsulta;
    @Column(name = "hora_proxima_consulta", nullable = false)
    private LocalTime horaProximaConsulta;
    @Column(name = "confirmado", nullable = false)
    private Boolean confirmado;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "justificativa")
    private String justificativa;
    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
}
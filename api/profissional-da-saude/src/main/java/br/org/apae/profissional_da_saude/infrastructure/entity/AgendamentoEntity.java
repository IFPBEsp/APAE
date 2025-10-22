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
    @Column(name = "profissional_id", nullable = false)
    private UUID idProfissional;

    @Column(name = "fk_atendimento", nullable = false)
    private UUID idAtendimento;
    @Column(name = "fk_cadastro_anual", nullable = false)
    private UUID idCadastroAnual;

    @Column(name = "frequencia_dias", nullable = false)
    private Integer frequenciaDias;
    @Column(name = "data_inicial", nullable = false)
    private LocalDate dataInicial;
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;
    @Column(name = "hora", nullable = false)
    private LocalTime hora;
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

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

package br.org.apae.profissional_da_saude.infrastructure.entity;

import br.org.apae.profissional_da_saude.domain.model.HistoricoConsulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "historico_consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "id_agendamento", nullable = false)
    private UUID idAgendamento;

    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;

    @Column(name = "hora_consulta", nullable = false)
    private LocalTime horaConsulta;

    @Column(name = "foi_realizada", nullable = false)
    private boolean foiRealizada;

    @Column(name = "justificativa")
    private String justificativa;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    public static HistoricoConsultaEntity fromDomain(HistoricoConsulta historico) {
        return new HistoricoConsultaEntity(
                historico.getId(),
                historico.getIdAgendamento(),
                historico.getDataConsulta(),
                historico.getHoraConsulta(),
                historico.isFoiRealizada(),
                historico.getJustificativa(),
                historico.getDataCriacao()
        );
    }

    public HistoricoConsulta toDomain() {
        return new HistoricoConsulta(
                this.id,
                this.idAgendamento,
                this.dataConsulta,
                this.horaConsulta,
                this.foiRealizada,
                this.justificativa,
                this.dataCriacao
        );
    }
}
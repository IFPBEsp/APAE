package br.org.apae.profissional_da_saude.infrastructure.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faltas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaltaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "justificada", nullable = false)
    private Boolean justificada;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "fk_atendimento", nullable = false)
    private UUID fkAtendimento;

    @Column(name = "fk_profissional", nullable = false)
    private UUID fkProfissional;

    @Column(name = "fk_cadastro_anual", nullable = false)
    private UUID fkCadastroAnual;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

}

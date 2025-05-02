package br.org.apae.documentos_digitalizados.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class DocumentosDigitalizados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomeBucket;

    @Column(nullable = false, unique = true)
    private Long pacienteId;

    @Column(nullable = false)
    private String nomePaciente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPaciente tipoPaciente;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false)
    private String documento;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;
}

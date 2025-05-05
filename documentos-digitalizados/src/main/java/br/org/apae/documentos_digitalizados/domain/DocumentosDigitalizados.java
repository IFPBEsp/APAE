package br.org.apae.documentos_digitalizados.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class DocumentosDigitalizados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nomeBucket;

    @Column(nullable = false)
    private UUID pacienteId;

    @Column(nullable = false)
    private String nomePaciente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPaciente tipoPaciente;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(nullable = false)
    private LocalDateTime dataAtualizacao;

    private String rotaDocumentos;
}

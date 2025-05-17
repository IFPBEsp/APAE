package br.org.apae.documentos_escolares.domain.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documentos_escolares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentosEscolares {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID pacienteId;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false)
    private String nomeArquivo;

    private String caminhoArquivo;

    private LocalDateTime dataUpload;
}

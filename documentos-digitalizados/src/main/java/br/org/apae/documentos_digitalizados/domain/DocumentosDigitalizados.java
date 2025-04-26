package br.org.apae.documentos_digitalizados.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class DocumentosDigitalizados {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pacienteId;

    @Column(nullable = false, unique = true)
    private UUID encaminhamento = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private UUID laudoMedico = UUID.randomUUID();
}

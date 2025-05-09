package br.org.apae.api_crud_pacientes.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "cadastro_anual")
public class CadastroAnual {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
}

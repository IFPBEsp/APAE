package br.org.apae.profissional_da_saude.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PacienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;
    private String email;
    private String telefone;
    private LocalDate dateNascimento;
    private String cpf;
    private String rg;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private LocalDateTime dataCriacao;
}

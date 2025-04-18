package br.org.apae.api_crud_pacientes.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "fichas_medicas")
public class FichaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne(mappedBy = "fichaMedica", cascade = CascadeType.ALL)
    private Paciente paciente;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}

package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "parentes")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "rg", nullable = false)
    private String rg;

    @Column(name = "cpf", nullable = false)
    private String cpf;

    @Column(name = "vivo", nullable = false)
    private boolean isAlive;

    @Column(name = "profissao", nullable = false)
    private String profession;

    @Column(name = "parentesco", nullable = false)
    private String kinship;

    @Column(name = "paciente_id", nullable = false)
    private UUID patientId;

    protected Parent() {
    }

    public Parent(String name, String rg, String cpf, boolean isAlive, String profession, String kinship,
            UUID patientId) {
        this.name = name;
        this.rg = rg;
        this.cpf = cpf;
        this.isAlive = isAlive;
        this.profession = profession;
        this.kinship = kinship;
        this.patientId = patientId;
    }

    public Parent(UUID id, String name, String rg, String cpf, boolean isAlive, String profession, String kinship,
            UUID patientId) {
        this.id = id;
        this.name = name;
        this.rg = rg;
        this.cpf = cpf;
        this.isAlive = isAlive;
        this.profession = profession;
        this.kinship = kinship;
        this.patientId = patientId;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRg() {
        return rg;
    }

    public String getCpf() {
        return cpf;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getProfession() {
        return profession;
    }

    public String getKinship() {
        return kinship;
    }

    public UUID getPatientId() {
        return patientId;
    }
}

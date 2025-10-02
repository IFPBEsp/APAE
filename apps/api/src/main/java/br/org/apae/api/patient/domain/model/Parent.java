package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.dto.paciente.dto.create.CreateParentDTO;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pais")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column
    private String rg;

    @Column
    private String cpf;

    @Column(name = "vivo", nullable = false)
    private boolean isAlive;

    @Column(name = "profissao")
    private String profession;

    @Column(name = "parentesco", nullable = false)
    private String kinship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Patient patient;

    protected Parent() {
    }

    private Parent(Builder builder) {
        this.name = builder.name;
        this.rg = builder.rg;
        this.cpf = builder.cpf;
        this.isAlive = builder.isAlive;
        this.profession = builder.profession;
        this.kinship = builder.kinship;
        this.patient = builder.patient;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getRg() { return rg; }
    public String getCpf() { return cpf; }
    public boolean isAlive() { return isAlive; }
    public String getProfession() { return profession; }
    public String getKinship() { return kinship; }
    public Patient getPatient() { return patient; }

    void setPatient(Patient patient) {
        this.patient = patient;
    }

    public static Parent from(CreateParentDTO dto, Patient patient) {
        if (dto == null) return null;
        return Parent.builder()
                .name(dto.name())
                .rg(dto.rg())
                .cpf(dto.cpf())
                .isAlive(dto.isAlive())
                .profession(dto.profession())
                .kinship(dto.kinship())
                .patient(patient)
                .build();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String rg;
        private String cpf;
        private boolean isAlive;
        private String profession;
        private String kinship;
        private Patient patient;

        public Builder name(String name) { this.name = name; return this; }
        public Builder rg(String rg) { this.rg = rg; return this; }
        public Builder cpf(String cpf) { this.cpf = cpf; return this; }
        public Builder isAlive(boolean isAlive) { this.isAlive = isAlive; return this; }
        public Builder profession(String profession) { this.profession = profession; return this; }
        public Builder kinship(String kinship) { this.kinship = kinship; return this; }
        public Builder patient(Patient patient) { this.patient = patient; return this; }

        public Parent build() {
            Objects.requireNonNull(name, "O nome do pai/responsável não pode ser nulo.");
            Objects.requireNonNull(kinship, "O parentesco não pode ser nulo.");
            Objects.requireNonNull(patient, "O paciente associado não pode ser nulo.");

            return new Parent(this);
        }
    }
}

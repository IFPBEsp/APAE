package br.org.apae.api.patient.domain.model;

import br.org.apae.api.common.dto.paciente.dto.create.CreateGuardianDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdateGuardianDTO;
import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "responsaveis")
public class Guardian {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "contato", nullable = false)
    private String contact;

    @Column(name = "parentesco", nullable = false)
    private String kinship;

    protected Guardian() {
    }

    private Guardian(Builder builder) {
        this.name = builder.name;
        this.contact = builder.contact;
        this.kinship = builder.kinship;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getKinship() { return kinship; }

    public static Guardian from(CreateGuardianDTO dto) {
        if (dto == null) return null;
        return Guardian.builder()
                .name(dto.name())
                .contact(dto.contact())
                .kinship(dto.kinship())
                .build();
    }

    public void updateWith(UpdateGuardianDTO dto) {
        if (dto == null) return;
        this.name = dto.name();
        this.contact = dto.contact();
        this.kinship = dto.kinship();
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String contact;
        private String kinship;

        public Builder name(String name) { this.name = name; return this; }
        public Builder contact(String contact) { this.contact = contact; return this; }
        public Builder kinship(String kinship) { this.kinship = kinship; return this; }

        public Guardian build() {
            Objects.requireNonNull(name, "O nome do responsável não pode ser nulo.");
            Objects.requireNonNull(contact, "O contato do responsável não pode ser nulo.");
            Objects.requireNonNull(kinship, "O parentesco do responsável não pode ser nulo.");

            return new Guardian(this);
        }
    }
}
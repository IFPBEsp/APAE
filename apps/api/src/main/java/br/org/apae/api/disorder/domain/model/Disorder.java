package br.org.apae.api.disorder.domain.model;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import jakarta.persistence.*;

import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "transtornos")
public class Disorder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    protected Disorder() {}

    private Disorder(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
    }

    public static Disorder from(CreateDisorderDTO dto) {
        return Disorder.builder()
                .name(dto.name())
                .description(dto.description())
                .build();
    }

    public void updateWith(UpdateDisorderDTO dto) {
        Optional.ofNullable(dto.name()).ifPresent(value -> this.name = value);
        Optional.ofNullable(dto.description()).ifPresent(value -> this.description = value);
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String name;
        private String description;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Disorder build() {
            return new Disorder(this);
        }
    }
}
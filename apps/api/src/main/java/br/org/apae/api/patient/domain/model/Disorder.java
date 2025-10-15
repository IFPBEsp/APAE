package br.org.apae.api.patient.domain.model;

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

    public Disorder(String name, String description) {
        this.name = name;
        this.description = description;
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
}
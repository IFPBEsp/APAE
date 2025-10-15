package br.org.apae.api.patient.domain.model;

import jakarta.persistence.*;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "transtornos")
public class Disorder {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column
    private String description;

    protected Disorder() {}

    public Disorder(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateDetails(String name, String description) {
        Optional.ofNullable(name).ifPresent(value -> this.name = value);
        Optional.ofNullable(description).ifPresent(value -> this.description = value);
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
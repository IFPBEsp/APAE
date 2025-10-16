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

    protected Disorder() {}

    public Disorder(String name) {
        this.name = name;
    }

    public void updateDetails(String name) {
        Optional.ofNullable(name).ifPresent(value -> this.name = value);
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
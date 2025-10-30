package br.org.apae.api.patient.domain.model;

import java.util.Optional;
import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "transtornos")
public class Disorder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true, length = 150)
    private String name;

    protected Disorder() {}

    public Disorder(String name) {
        this.name = name;
    }

    public void setName(String name) {
        Optional.ofNullable(name).ifPresent(value -> this.name = value);
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
}
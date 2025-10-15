package br.org.apae.api.patient.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "transtornos")
public class Disorder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, unique = true, length = 150)
    private String name;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String description;

    @Deprecated
    protected Disorder() {}

    public Disorder(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do transtorno não pode ser nulo ou vazio.");
        }
        this.name = name;
        this.description = description;
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

    public void updateDetails(String newName, String newDescription) {
        if (newName != null && !newName.trim().isEmpty()) {
            this.name = newName;
        }
        this.description = newDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disorder disorder = (Disorder) o;
        return Objects.equals(id, disorder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
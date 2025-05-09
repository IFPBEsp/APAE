package br.org.apae.documentos_pessoais_digitalizados.domain.model;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_documentos_pessoais")
public class PersonalDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonalDocumentType personalDocumentType;
    @Column(nullable = false)
    private String pathDocumentStorage;
    @Column(nullable = false)
    private String contentType;
    @Column(nullable = false)
    private UUID patient;

    public PersonalDocument() {
    }

    public PersonalDocument(PersonalDocumentType personalDocumentType, String pathDocumentStorage, String contentType, UUID patient) {
        this.personalDocumentType = personalDocumentType;
        this.pathDocumentStorage = pathDocumentStorage;
        this.contentType = contentType;
        this.patient = patient;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PersonalDocumentType getPersonalDocumentType() {
        return personalDocumentType;
    }

    public void setPersonalDocumentType(PersonalDocumentType personalDocumentType) {
        this.personalDocumentType = personalDocumentType;
    }

    public String getPathDocumentStorage() {
        return pathDocumentStorage;
    }

    public void setPathDocumentStorage(String pathDocumentStorage) {
        this.pathDocumentStorage = pathDocumentStorage;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public UUID getPatient() {
        return patient;
    }

    public void setPatient(UUID patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PersonalDocument that = (PersonalDocument) o;
        return Objects.equals(getId(), that.getId()) && getPersonalDocumentType() == that.getPersonalDocumentType() && Objects.equals(getPathDocumentStorage(), that.getPathDocumentStorage()) && Objects.equals(getContentType(), that.getContentType()) && Objects.equals(getPatient(), that.getPatient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPersonalDocumentType(), getPathDocumentStorage(), getContentType(), getPatient());
    }
}

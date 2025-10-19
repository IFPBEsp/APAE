package br.org.apae.api.patient.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {

}

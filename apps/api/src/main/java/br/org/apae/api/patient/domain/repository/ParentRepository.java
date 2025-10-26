package br.org.apae.api.patient.domain.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.Parent;

@Repository
public interface ParentRepository extends JpaRepository<Parent, UUID> {
  List<Parent> findAllByPatientId(UUID patientId);
}

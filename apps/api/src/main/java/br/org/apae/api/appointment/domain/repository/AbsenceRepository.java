package br.org.apae.api.appointment.domain.repository;

import br.org.apae.api.appointment.domain.model.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, UUID> {}
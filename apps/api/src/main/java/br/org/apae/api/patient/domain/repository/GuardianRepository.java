package br.org.apae.api.patient.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.apae.api.patient.domain.model.Guardian;

@Repository
public interface GuardianRepository extends JpaRepository<Guardian, UUID> {

}

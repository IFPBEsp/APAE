package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Disorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisorderRepository extends JpaRepository<Disorder, Long> {
    Optional<Disorder> findByName(String name);
}

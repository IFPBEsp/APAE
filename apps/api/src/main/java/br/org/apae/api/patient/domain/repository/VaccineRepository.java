package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    // Método para buscar uma vacina pelo nome, útil para evitar duplicatas
    Optional<Vaccine> findByName(String name);

}
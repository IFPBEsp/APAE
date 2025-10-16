package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Disorder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DisorderRepository extends JpaRepository<Disorder, UUID> {

    Optional<Disorder> findByName(String name);

    List<Disorder> findByNameInIgnoreCase(Collection<String> names);
}
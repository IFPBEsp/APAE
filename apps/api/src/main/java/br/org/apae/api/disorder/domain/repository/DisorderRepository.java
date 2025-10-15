package br.org.apae.api.disorder.domain.repository;

import br.org.apae.api.disorder.domain.model.Disorder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DisorderRepository extends JpaRepository<Disorder, UUID>{
}

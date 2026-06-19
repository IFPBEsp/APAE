package br.org.apae.api.servicetype.domain.repository;

import br.org.apae.api.servicetype.domain.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {

    boolean existsByArea(String area);

    Optional<ServiceType> findByArea(String area);

    Set<ServiceType> findByAreaIn(Set<String> areas);
}


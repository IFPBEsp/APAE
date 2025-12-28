package br.org.apae.api.servicearea.domain.repository;

import br.org.apae.api.servicearea.domain.model.ServiceArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceAreaRepository extends JpaRepository<ServiceArea, Integer> {

    boolean existsByArea(String area);

    Optional<ServiceArea> findByArea(String area);
}


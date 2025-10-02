package br.org.apae.profissional_da_saude.domain.repository;

import br.org.apae.profissional_da_saude.domain.model.AreaSaude;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AreaSaudeRepository {

    AreaSaude save(AreaSaude areaSaude);

    Page<AreaSaude> findAll(Pageable pageable);

    Optional<AreaSaude> findById(Integer id);


    void deleteById(Integer id);

    boolean existsByArea(String area);
}

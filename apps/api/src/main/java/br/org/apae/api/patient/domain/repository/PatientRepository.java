package br.org.apae.api.patient.domain.repository;

import br.org.apae.api.patient.domain.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {
  boolean existsByCpfOrRg(String cpf, String rg);

    @Query("SELECT DISTINCT p.address.city FROM Patient p WHERE p.address.city IS NOT NULL AND p.address.city <> '' ORDER BY p.address.city ASC")
    List<String> findDistinctCities();


}

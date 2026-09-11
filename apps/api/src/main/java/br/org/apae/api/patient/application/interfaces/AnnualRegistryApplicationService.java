package br.org.apae.api.patient.application.interfaces;

import java.time.Year;
import java.util.List;
import java.util.UUID;
import br.org.apae.api.common.dto.patient.request.annualregistry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annualregistry.AnnualRegistryResponseDTO;

public interface AnnualRegistryApplicationService {

    AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDto, UUID patientId);

    AnnualRegistryResponseDTO findRegistryByPatientAndYear(UUID patientId, Year year);

    AnnualRegistryResponseDTO updateRegistry(UUID patientId, UUID registryId, UpdateAnnualRegistryDTO updateDto);

    AnnualRegistryResponseDTO replaceRegistry(UUID patientId, UUID registryId, ReplaceAnnualRegistryDTO replaceDto);

    void deleteRegistry(UUID patientId, UUID registryId);

    List<String> findAllRegistryYears();

    void deleteAllRegistriesByPatient(UUID patientId);

    List<Integer> listYearsByPatient(UUID patientId);
}
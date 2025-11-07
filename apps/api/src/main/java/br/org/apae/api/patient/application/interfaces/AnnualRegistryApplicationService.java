package br.org.apae.api.patient.application.interfaces;

import java.util.UUID;
import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;

public interface AnnualRegistryApplicationService {

    AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDto, UUID patientId);

    /**
     * Busca um registro anual pelo ID do paciente e o ano.
     */
    AnnualRegistryResponseDTO findRegistryByPatientAndYear(UUID patientId, Integer year);

    /**
     * Atualiza um registro anual existente.
     */
    AnnualRegistryResponseDTO updateRegistry(UUID patientId, UUID registryId, UpdateAnnualRegistryDTO updateDto);

    /**
     * Deleta um registro anual específico pelo seu ID.
     */
    void deleteRegistry(UUID patientId, UUID registryId);

    /**
     * Deleta TODOS os registros anuais de um paciente (ação em cascata).
     */
    void deleteAllRegistriesByPatient(UUID patientId); // Renomeei seu método deleteRegistry antigo
}
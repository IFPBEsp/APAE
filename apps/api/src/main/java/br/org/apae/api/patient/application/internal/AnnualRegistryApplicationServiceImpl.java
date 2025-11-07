package br.org.apae.api.patient.application.internal;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.patient.domain.exceptions.RegistryNotFoundException;
import br.org.apae.api.patient.domain.exceptions.RegistryOwnershipException;
import org.springframework.stereotype.Service;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.mappers.AnnualRegistryMapper;
import br.org.apae.api.patient.domain.exceptions.AnnualRegistryConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderMismatchException;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnualRegistryApplicationServiceImpl implements AnnualRegistryApplicationService {

    private final AnnualRegistryRepository annualRegistryRepository;
    private final AnnualRegistryMapper annualRegistryMapper;
    private final DisorderApplicationService disorderService;
    private final PatientDomainService patientDomainService;

    public AnnualRegistryApplicationServiceImpl(AnnualRegistryRepository annualRegistryRepository,
                                                AnnualRegistryMapper annualRegistryMapper, DisorderApplicationService disorderService,
                                                PatientDomainService patientDomainService) {
        this.annualRegistryRepository = annualRegistryRepository;
        this.annualRegistryMapper = annualRegistryMapper;
        this.disorderService = disorderService;
        this.patientDomainService = patientDomainService;
    }

    @Override
    @Transactional
    public AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDTO, UUID patientId) {
        patientDomainService.getByIdOrThrow(patientId);

        annualRegistryRepository
                .findByPatientIdAndYear(patientId, createAnnualRegistryDTO.year())
                .ifPresent(registry -> {
                    throw new AnnualRegistryConflictException(createAnnualRegistryDTO.year());
                });

        Set<DisorderResponseDTO> disorderDtos = disorderService
                .findDisorders(createAnnualRegistryDTO.disorders());

        if (createAnnualRegistryDTO.disorders().size() != disorderDtos.size()) {
            throw new DisorderMismatchException();
        }

        AnnualRegistry registry = annualRegistryMapper.toEntity(createAnnualRegistryDTO, disorderDtos, patientId);
        AnnualRegistry registrySaved = annualRegistryRepository.save(registry);

        return annualRegistryMapper.toResponseDTO(registrySaved);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnualRegistryResponseDTO findRegistryByPatientAndYear(UUID patientId, Integer year) {
        patientDomainService.getByIdOrThrow(patientId);

        AnnualRegistry registry = annualRegistryRepository
                .findByPatientIdAndYear(patientId, year)
                .orElseThrow(() -> new RegistryNotFoundException(year));

        return annualRegistryMapper.toResponseDTO(registry);
    }

    @Override
    @Transactional
    public AnnualRegistryResponseDTO updateRegistry(UUID patientId, UUID registryId, UpdateAnnualRegistryDTO updateDto) {
        patientDomainService.getByIdOrThrow(patientId);

        AnnualRegistry registry = annualRegistryRepository.findById(registryId)
                .orElseThrow(() -> new RegistryNotFoundException(registryId));

        if (!registry.getPatientId().equals(patientId)) {
            throw new RegistryOwnershipException(patientId, registryId);
        }

        if (updateDto.year() != null && !updateDto.year().equals(registry.getYear())) {
            annualRegistryRepository
                    .findByPatientIdAndYear(patientId, updateDto.year())
                    .ifPresent(existing -> {
                        throw new AnnualRegistryConflictException(updateDto.year());
                    });
        }

        Set<DisorderResponseDTO> disorderDtos = null;
        if (updateDto.disorders() != null) {
            disorderDtos = disorderService.findDisordersFromUpdateDTOs(updateDto.disorders());

            if (updateDto.disorders().size() != disorderDtos.size()) {
                throw new DisorderMismatchException();
            }
        }

        AnnualRegistry updatedRegistry = annualRegistryMapper.updateEntityFromDto(registry, updateDto, disorderDtos);

        AnnualRegistry registrySaved = annualRegistryRepository.save(updatedRegistry);
        return annualRegistryMapper.toResponseDTO(registrySaved);
    }

    @Override
    @Transactional
    public AnnualRegistryResponseDTO replaceRegistry(UUID patientId, UUID registryId, ReplaceAnnualRegistryDTO replaceDto) {
        patientDomainService.getByIdOrThrow(patientId);

        AnnualRegistry registry = annualRegistryRepository.findById(registryId)
                .orElseThrow(() -> new RegistryNotFoundException(registryId));

        if (!registry.getPatientId().equals(patientId)) {
            throw new RegistryOwnershipException(patientId, registryId);
        }

        Set<DisorderResponseDTO> disorderDtos = disorderService
                .findDisorders(replaceDto.disorders());

        if (replaceDto.disorders().size() != disorderDtos.size()) {
            throw new DisorderMismatchException();
        }

        AnnualRegistry replacedRegistry = annualRegistryMapper.replaceEntityFromDto(registry, replaceDto, disorderDtos);

        AnnualRegistry registrySaved = annualRegistryRepository.save(replacedRegistry);
        return annualRegistryMapper.toResponseDTO(registrySaved);
    }


    @Override
    @Transactional
    public void deleteRegistry(UUID patientId, UUID registryId) {
        patientDomainService.getByIdOrThrow(patientId);

        AnnualRegistry registry = annualRegistryRepository.findById(registryId)
                .orElseThrow(() -> new RegistryNotFoundException(registryId));

        if (!registry.getPatientId().equals(patientId)) {
            throw new RegistryOwnershipException(patientId, registryId);
        }

        annualRegistryRepository.delete(registry);
    }

    @Override
    @Transactional
    public void deleteAllRegistriesByPatient(UUID patientId) {
        List<AnnualRegistry> registries = annualRegistryRepository.findAllByPatientId(patientId);

        if (registries.isEmpty()) {
            return;
        }

        annualRegistryRepository.deleteAll(registries);
    }
}
package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderService;
import br.org.apae.api.patient.application.mappers.DisorderMapper;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.repository.DisorderRepository;
import br.org.apae.api.patient.exception.types.DisorderAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisorderServiceImpl implements DisorderService {

    private final DisorderRepository repository;
    private final DisorderMapper mapper;

    public DisorderServiceImpl(DisorderRepository repository, DisorderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DisorderResponseDTO save(CreateDisorderDTO dto) {
        validateNameUniqueness(dto.name());

        Disorder disorder = mapper.toEntity(dto);
        Disorder savedDisorder = repository.save(disorder);

        return mapper.toResponseDTO(savedDisorder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisorderResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DisorderResponseDTO findById(UUID id) {
        Disorder disorder = findDisorderOrThrow(id);
        return mapper.toResponseDTO(disorder);
    }

    @Override
    public DisorderResponseDTO update(UUID id, UpdateDisorderDTO dto) {
        Disorder disorder = findDisorderOrThrow(id);

        if (dto.name() != null) {
            validateNameUniquenessExcludingId(dto.name(), id);
            disorder.updateDetails(dto.name());
        }

        Disorder updatedDisorder = repository.save(disorder);
        return mapper.toResponseDTO(updatedDisorder);
    }

    @Override
    public void delete(UUID id) {
        Disorder disorder = findDisorderOrThrow(id);
        repository.delete(disorder);
    }

    private void validateNameUniqueness(String name) {
        if (repository.findByNameIgnoreCase(name).isPresent()) {
            throw new DisorderAlreadyExistsException(name);
        }
    }

    private void validateNameUniquenessExcludingId(String name, UUID idToExclude) {
        if (repository.existsByNameIgnoreCaseAndIdNot(name, idToExclude)) {
            throw new DisorderAlreadyExistsException(name);
        }
    }

    private Disorder findDisorderOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transtorno com ID " + id + " não encontrado."));
    }
}
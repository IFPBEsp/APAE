package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderService;
import br.org.apae.api.patient.application.mappers.DisorderMapper;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.repository.DisorderRepository;
import br.org.apae.api.patient.exception.types.DisorderAlreadyExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
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
    public Set<DisorderResponseDTO> findDisordersByNames(Set<CreateDisorderDTO> createDisorderDTOs) {
        Set<String> names = createDisorderDTOs.stream()
                .map(CreateDisorderDTO::name)
                .collect(Collectors.toSet());

        Set<Disorder> disorders = repository.findByNameIn(names);

        return disorders.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisorderResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
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
            disorder.setName(dto.name());
        }

        return mapper.toResponseDTO(repository.save(disorder));
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
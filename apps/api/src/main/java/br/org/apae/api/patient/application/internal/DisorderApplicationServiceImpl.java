package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.mappers.DisorderMapper;
import br.org.apae.api.patient.domain.exceptions.DisorderConflictException;
import br.org.apae.api.patient.domain.exceptions.DisorderInUseException;
import br.org.apae.api.patient.domain.exceptions.DisorderNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.repository.DisorderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DisorderApplicationServiceImpl implements DisorderApplicationService {

    private final DisorderRepository repository;
    private final DisorderMapper mapper;

    public DisorderApplicationServiceImpl(DisorderRepository repository, DisorderMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public DisorderResponseDTO createDisorder(CreateDisorderDTO createDisorderDto) {
        if (repository.findByNameIgnoreCase(createDisorderDto.name()).isPresent()) {
            throw new DisorderConflictException(createDisorderDto.name());
        }

        Disorder disorder = mapper.toEntity(createDisorderDto);
        Disorder savedDisorder = repository.save(disorder);

        return mapper.toResponseDTO(savedDisorder);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<DisorderResponseDTO> findDisorders(Set<CreateDisorderDTO> createDisorderDtos) {
        Set<String> names = createDisorderDtos.stream()
                .map(CreateDisorderDTO::name)
                .collect(Collectors.toSet());

        Set<Disorder> disorders = repository.findByNameIn(names);

        return disorders.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<DisorderResponseDTO> findDisordersFromUpdateDTOs(Set<UpdateDisorderDTO> updateDisorderDtos) {
        Set<CreateDisorderDTO> createDisorderDtos = updateDisorderDtos.stream()
                .map(updateDto -> new CreateDisorderDTO(updateDto.name()))
                .collect(Collectors.toSet());

        return this.findDisorders(createDisorderDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DisorderResponseDTO> findAllDisorders() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DisorderResponseDTO findDisorderById(UUID id) {
        Disorder disorder = findDisorderOrThrow(id);
        return mapper.toResponseDTO(disorder);
    }

    @Override
    @Transactional
    public DisorderResponseDTO updateDisorder(UUID id, UpdateDisorderDTO dto) {
        Disorder disorder = findDisorderOrThrow(id);

        if (dto.name() != null) {
            if (repository.existsByNameIgnoreCaseAndIdNot(dto.name(), id)) {
                throw new DisorderConflictException(dto.name());
            }
            disorder.setName(dto.name());
        }

        return mapper.toResponseDTO(repository.save(disorder));
    }

    @Override
    @Transactional
    public void deleteDisorder(UUID id) {
        if (!repository.existsById(id)) {
            throw new DisorderNotFoundException();
        }

        try {
            repository.deleteById(id);
            repository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new DisorderInUseException();
        }
    }

    private Disorder findDisorderOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(DisorderNotFoundException::new);
    }
}

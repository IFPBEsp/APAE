package br.org.apae.api.disorder.application.internal;

import br.org.apae.api.disorder.application.interfaces.DisorderService;
import br.org.apae.api.disorder.domain.model.Disorder;
import br.org.apae.api.disorder.domain.repository.DisorderRepository;
import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DisorderServiceImpl implements DisorderService {

    private final DisorderRepository repository;

    public DisorderServiceImpl(DisorderRepository repository) {
        this.repository = repository;
    }

    private DisorderResponseDTO mapToDTO(Disorder disorder) {
        return new DisorderResponseDTO(
                disorder.getId(),
                disorder.getName(),
                disorder.getDescription()
        );
    }

    @Override
    public DisorderResponseDTO save(CreateDisorderDTO dto) {
        Disorder disorder = Disorder.from(dto);
        Disorder savedDisorder = repository.save(disorder);

        return mapToDTO(savedDisorder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisorderResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public DisorderResponseDTO findById(UUID id) {
        Disorder disorder = findDisorderOrThrow(id);
        return mapToDTO(disorder);
    }

    @Override
    public DisorderResponseDTO update(UUID id, UpdateDisorderDTO dto) {
        Disorder disorder = findDisorderOrThrow(id);
        disorder.updateWith(dto);

        return mapToDTO(disorder);
    }

    @Override
    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Transtorno com ID " + id + " não encontrado.");
        }
        repository.deleteById(id);
    }

    private Disorder findDisorderOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transtorno com ID " + id + " não encontrado."));
    }
}
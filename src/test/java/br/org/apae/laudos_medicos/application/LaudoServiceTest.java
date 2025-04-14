package br.org.apae.laudos_medicos.application;

import br.org.apae.laudos_medicos.application.dtos.requests.LaudoRequestDTO;
import br.org.apae.laudos_medicos.application.services.LaudoService;
import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.domain.repositories.LaudoRepository;
import br.org.apae.laudos_medicos.infrastructure.clients.MedicoClient;
import br.org.apae.laudos_medicos.infrastructure.clients.PacienteClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LaudoServiceTest {
    @InjectMocks
    private LaudoService laudoService;

    @Mock
    private LaudoRepository laudoRepository;

    @Mock
    private MedicoClient medicoClient;

    @Mock
    private PacienteClient pacienteClient;

    @Test
    void deveSalvarLaudo() {
        Long idMedico = 2L;
        Long idPaciente = 3L;
        LocalDate dataEmissao = LocalDate.parse("2024-12-15");
        String descricao = "Laudo";
        LaudoRequestDTO laudoRequestDTO = new LaudoRequestDTO(
                idMedico,
                idPaciente,
                dataEmissao,
                descricao
        );

        when(medicoClient.buscarMedicoPorId(idMedico)).thenReturn(new MedicoResponseDTO(idMedico, "Medico"));
        when(pacienteClient.buscarPacientePorId(idPaciente)).thenReturn(new PacienteResponseDTO(idPaciente, "Paciente"));
        when(laudoRepository.save(any(Laudo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laudo laudo = laudoService.criarNovoLaudo(laudoRequestDTO);

        verify(laudoRepository, times(1)).save(any(Laudo.class));
        assertNotNull(laudo);
        assertEquals(idMedico, laudo.getIdMedico());
        assertEquals(idPaciente, laudo.getIdPaciente());
        assertEquals(descricao, laudo.getDescricao());
        assertEquals(dataEmissao, laudo.getDataEmissao());
    }

    @Test
    void deveLancarExcecaoSeIdPacienteInvalido() {
        Long idMedico = 1L;
        Long idPaciente = 999L;
        LocalDate dataEmissao = LocalDate.parse("2024-12-15");
        String descricao = "Laudo";
        LaudoRequestDTO laudoRequestDTO = new LaudoRequestDTO(
                idMedico,
                idPaciente,
                dataEmissao,
                descricao
        );

        when(pacienteClient.buscarPacientePorId(idPaciente))
                .thenThrow(new IdPacienteInvalidoException("Id do paciente não existe ou não encontrado."));

        IdPacienteInvalidoException exception = assertThrows(IdPacienteInvalidoException.class, () -> laudoService.criarNovoLaudo(laudoRequestDTO));

        assertEquals("Id do paciente não existe ou não encontrado.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeIdMedicoInvalido() {
        Long idMedico = 10L;
        Long idPaciente = 3L;
        LocalDate dataEmissao = LocalDate.parse("2024-12-15");
        String descricao = "Laudo";
        LaudoRequestDTO laudoRequestDTO = new LaudoRequestDTO(
                idMedico,
                idPaciente,
                dataEmissao,
                descricao
        );

        when(medicoClient.buscarMedicoPorId(idMedico))
                .thenThrow(new IdMedicoInvalidoException("Id do médico não existe ou não encontrado."));

        IdMedicoInvalidoException exception = assertThrows(IdMedicoInvalidoException.class, () -> laudoService.criarNovoLaudo(laudoRequestDTO));

        assertEquals("Id do médico não existe ou não encontrado.", exception.getMessage());
    }

    @Test
    void deveRetornarLaudoPorId() {
        Long id = 1L;
        Laudo laudo = new Laudo(id, 2L, 3L, LocalDate.now(), "Laudo de teste");

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudo));

        Laudo resultado = laudoService.buscarLaudoPorId(id);

        assertNotNull(resultado);
        assertEquals("Laudo de teste", resultado.getDescricao());
        assertEquals(id, resultado.getId());
    }

    @Test
    void deveLancarExcecaoSeIdLaudoNaoEncontrado() {
        Long id = 8L;

        when(laudoRepository.findById(id)).thenReturn(Optional.empty());

        LaudoNaoEncontradoException exception = assertThrows(LaudoNaoEncontradoException.class, () -> laudoService.buscarLaudoPorId(id));

        assertEquals("Laudo não encontrado com esse id.", exception.getMessage());
    }

    @Test
    void deveRetornarLaudosPorIdPaciente() {
        Long idPaciente = 2L;
        List<Laudo> laudos = List.of(
                new Laudo(1L, 2L, 3L, LocalDate.now(), "Descrição 1"),
                new Laudo(2L, 2L, 4L, LocalDate.now(), "Descrição 2")
        );

        when(pacienteClient.buscarPacientePorId(idPaciente))
                .thenReturn(new PacienteResponseDTO(idPaciente, "Paciente 1"));

        when(laudoRepository.findLaudosByPacientId(idPaciente))
                .thenReturn(laudos);

        List<Laudo> resultados = laudoService.buscarLaudosPorIdPaciente(idPaciente);

        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertEquals(idPaciente, resultados.get(0).getIdPaciente());
        assertEquals(idPaciente, resultados.get(1).getIdPaciente());
    }

    @Test
    void deveRetornarTodosOsLaudos() {
        List<Laudo> laudos = List.of(
                new Laudo(1L, 2L, 3L, LocalDate.now(), "Descrição 1"),
                new Laudo(2L, 3L, 4L, LocalDate.now(), "Descrição 2")
        );

        when(laudoRepository.findAll()).thenReturn(laudos);

        List<Laudo> resultado = laudoService.buscarTodosOsLaudos();

        assertEquals(2, resultado.size());
        assertEquals("Descrição 1", resultado.get(0).getDescricao());
        assertEquals("Descrição 2", resultado.get(1).getDescricao());
    }

    @Test
    void deveAtualizarLaudoComSucesso() {
        Long id = 1L;
        Laudo laudoExistente = new Laudo(id, 2L, 3L, LocalDate.now(), "Antiga descrição");

        LaudoRequestDTO novoDto = new LaudoRequestDTO(
                2L,
                3L,
                LocalDate.of(2025, 1, 1),
                "Nova descrição"
        );

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudoExistente));
        when(laudoRepository.save(any(Laudo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Laudo atualizado = laudoService.atualizarLaudo(id, novoDto);

        assertNotNull(atualizado);
        assertEquals("Nova descrição", atualizado.getDescricao());
        assertEquals(LocalDate.of(2025, 1, 1), atualizado.getDataEmissao());
    }

    @Test
    void deveLancarExcecaoAoAtualizarLaudoInexistente() {
        Long id = 100L;
        LaudoRequestDTO dto = new LaudoRequestDTO(1L, 2L, LocalDate.now(), "Descrição atualizada");

        when(laudoRepository.findById(id)).thenReturn(Optional.empty());

        LaudoNaoEncontradoException exception = assertThrows(LaudoNaoEncontradoException.class,
                () -> laudoService.atualizarLaudo(id, dto));

        assertEquals("Laudo não encontrado com esse id.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeIdMedicoInvalidoNoUpdate() {
        Long id = 1L;
        Laudo laudoExistente = new Laudo(id, 1L, 2L, LocalDate.now(), "Descrição antiga");
        LaudoRequestDTO dto = new LaudoRequestDTO(999L, 2L, LocalDate.now(), "Descrição atualizada");

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudoExistente));

        when(medicoClient.buscarMedicoPorId(999L))
                .thenThrow(new IdMedicoInvalidoException("Id do médico não existe ou não encontrado."));

        IdMedicoInvalidoException exception = assertThrows(IdMedicoInvalidoException.class,
                () -> laudoService.atualizarLaudo(id, dto));

        assertEquals("Id do médico não existe ou não encontrado.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeIdPacienteInvalidoNoUpdate() {
        Long id = 1L;
        Laudo laudoExistente = new Laudo(id, 1L, 2L, LocalDate.now(), "Descrição antiga");
        LaudoRequestDTO dto = new LaudoRequestDTO(1L, 999L, LocalDate.now(), "Descrição atualizada");

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudoExistente));

        when(pacienteClient.buscarPacientePorId(999L))
                .thenThrow(new IdPacienteInvalidoException("Id do paciente não existe ou não encontrado."));

        IdPacienteInvalidoException exception = assertThrows(IdPacienteInvalidoException.class,
                () -> laudoService.atualizarLaudo(id, dto));

        assertEquals("Id do paciente não existe ou não encontrado.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeDescricaoInvalidaNoUpdate() {
        Long id = 1L;
        Laudo laudoExistente = new Laudo(id, 1L, 2L, LocalDate.now(), "Descrição antiga");
        LaudoRequestDTO dto = new LaudoRequestDTO(1L, 2L, LocalDate.now(), " "); // descrição vazia

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudoExistente));

        DescricaoInvalidaException exception = assertThrows(DescricaoInvalidaException.class,
                () -> laudoService.atualizarLaudo(id, dto));

        assertEquals("Descrição inválida. A descrição não pode estar vazia ou em branco.", exception.getMessage());
    }


    @Test
    void deveDeletarLaudoComSucesso() {
        Long id = 1L;
        Laudo laudo = new Laudo(id, 2L, 3L, LocalDate.now(), "Descrição");

        when(laudoRepository.findById(id)).thenReturn(Optional.of(laudo));
        doNothing().when(laudoRepository).delete(id);

        assertDoesNotThrow(() -> laudoService.deletarLaudo(id));

        verify(laudoRepository, times(1)).delete(laudo);
    }

    @Test
    void deveLancarExcecaoAoDeletarLaudoInexistente() {
        Long id = 999L;

        when(laudoRepository.findById(id)).thenReturn(Optional.empty());
        doNothing().when(laudoRepository).delete(id);

        LaudoNaoEncontradoException exception = assertThrows(LaudoNaoEncontradoException.class, () -> laudoService.deleteLaudo(id));

        assertEquals("Laudo não encontrado com esse id.", exception.getMessage());

    }

}

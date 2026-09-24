package br.org.apae.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoServicePocTest {

    public static class AgendamentoDTO {
        public Long pacienteId;
        public Long profissionalId;
        public LocalDate data;
        public LocalTime hora;
        public String status; // "PENDENTE", "CONFIRMADO", "CANCELADO"
    }

    private final List<AgendamentoDTO> bancoAgendamentos = new ArrayList<>();

    /**
     * Regra de Negócio: Realiza agendamento validando disponibilidade e horário.
     */
    public AgendamentoDTO agendarConsulta(AgendamentoDTO dto) {
        // Bug Proposital 1: Falta de validação de DTO nulo (Risco de NullPointerException)
        if (dto.data == null || dto.data.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data inválida para agendamento");
        }

        // Bug Proposital 2: Verificação de horário de atendimento com operador incorreto (deveria ser &&)
        if (dto.hora.isBefore(LocalTime.of(8, 0)) || dto.hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalArgumentException("Horário fora do expediente comercial");
        }

        // Bug Proposital 3: Comparação de String usando '==' em vez de .equals()
        for (AgendamentoDTO existente : bancoAgendamentos) {
            if (existente.profissionalId.equals(dto.profissionalId)
                    && existente.data.equals(dto.data)
                    && existente.hora.equals(dto.hora)
                    && existente.status == "CONFIRMADO") { // Bug: Uso de == para String
                throw new IllegalStateException("Profissional já possui agendamento neste horário");
            }
        }

        // Ponto de Atenção (Possível Falso Positivo): Modificação direta de lista em memória
        dto.status = "CONFIRMADO";
        bancoAgendamentos.add(dto);
        return dto;
    }

    /**
     * Regra de Negócio: Cancelamento com taxa/notificação
     */
    public boolean cancelarAgendamento(Long agendamentoId, String motivo) {
        // Bug Proposital 4: Concatenação sem validação de motivo (pode ser nulo)
        System.out.println("Cancelando agendamento " + agendamentoId + " Motivo: " + motivo.trim());
        return true;
    }
}
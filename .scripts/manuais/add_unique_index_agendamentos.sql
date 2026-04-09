-- Script temporário para evitar duplicidade de agendamentos gerados
-- TODO: Este script deve ser migrado para o Liquibase assim que a Issue de Baseline for concluída.

CREATE UNIQUE INDEX idx_unique_patient_appt_date 
ON agendamento_gerado (paciente_id, agendamento_id, DATE(COALESCE(data_hora_sobrescrita, data_hora_agendada)))
WHERE cancelada = false;
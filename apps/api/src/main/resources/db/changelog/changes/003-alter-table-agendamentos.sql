-- 1. remover colunas antigas
ALTER TABLE agendamentos

DROP COLUMN IF EXISTS confirmado,
DROP COLUMN IF EXISTS hora_proxima_consulta,
DROP COLUMN IF EXISTS proxima_consulta,
DROP COLUMN IF EXISTS paciente_id,
DROP COLUMN IF EXISTS descricao,
DROP COLUMN IF EXISTS justificativa;

-- 2 adicionar colunas
ALTER TABLE agendamentos
ADD COLUMN data_inicial DATE NOT NULL;

ALTER TABLE agendamentos
ADD COLUMN data_final DATE NOT NULL;

ALTER TABLE agendamentos
ADD COLUMN atendimento_id UUID NOT NULL;

ALTER TABLE agendamentos
ADD COLUMN cadastro_anual_id UUID NOT NULL;

ALTER TABLE agendamentos
ADD COLUMN hora TIME NOT NULL;


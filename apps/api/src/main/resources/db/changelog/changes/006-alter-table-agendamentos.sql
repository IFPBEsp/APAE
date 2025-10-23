ALTER TABLE "agendamentos"
DROP COLUMN IF EXISTS confirmado,
DROP COLUMN IF EXISTS hora_proxima_consulta,
DROP COLUMN IF EXISTS proxima_consulta,
DROP COLUMN IF EXISTS paciente_id,
DROP COLUMN IF EXISTS descricao,
DROP COLUMN IF EXISTS justificativa;

ALTER TABLE "agendamentos"
ADD COLUMN data_inicial DATE NOT NULL;

ALTER TABLE "agendamentos"
ADD COLUMN data_final DATE NOT NULL;

ALTER TABLE "agendamentos"
ADD COLUMN atendimento_id UUID NOT NULL;

ALTER TABLE "agendamentos"
ADD COLUMN cadastro_anual_id UUID NOT NULL;

ALTER TABLE "agendamentos"
ADD COLUMN hora TIME NOT NULL;

ALTER TABLE "agendamentos"
ADD CONSTRAINT "fk_agendamentos_atendimento"
FOREIGN KEY (atendimento_id) REFERENCES "atendimento" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "agendamentos"
ADD CONSTRAINT "fk_agendamentos_cadastro_anual"
FOREIGN KEY (cadastro_anual_id) REFERENCES "cadastro_anual" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "agendamentos"
ADD CONSTRAINT "fk_agendamentos_profissional"
FOREIGN KEY (profissional_id) REFERENCES "profissionais_da_saude" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
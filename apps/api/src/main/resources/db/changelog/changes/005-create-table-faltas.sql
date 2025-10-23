CREATE TABLE "faltas" (
    "id" UUID NOT NULL,
    "data" DATE NOT NULL,
    "hora" TIME(6) WITHOUT TIME ZONE NOT NULL,
    "justificada" BOOLEAN NOT NULL,
    "motivo" VARCHAR(255),
    "atendimento_id" UUID NOT NULL,
    "profissional_id" UUID NOT NULL,
    "cadastro_anual_id" UUID NOT NULL,
    CONSTRAINT "faltas_pkey" PRIMARY KEY ("id")
);

ALTER TABLE "faltas"
ADD CONSTRAINT "FK_faltas_atendimento"
FOREIGN KEY ("atendimento_id") REFERENCES "atendimento" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "faltas"
ADD CONSTRAINT "FK_faltas_profissional"
FOREIGN KEY ("profissional_id") REFERENCES "profissionais_da_saude" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE "faltas"
ADD CONSTRAINT "FK_faltas_cadastro_anual"
FOREIGN KEY ("cadastro_anual_id") REFERENCES "cadastro_anual" ("id")
ON UPDATE NO ACTION ON DELETE NO ACTION;
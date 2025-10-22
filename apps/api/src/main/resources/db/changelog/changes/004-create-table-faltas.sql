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
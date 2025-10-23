CREATE TABLE "cadastro_anual" (
    "id" UUID NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
    "ano" INTEGER NOT NULL,
    "ativo" BOOLEAN NOT NULL DEFAULT TRUE,
    "paciente_id" UUID NOT NULL,
    CONSTRAINT "FK_cadastro_anual_paciente" FOREIGN KEY ("paciente_id") REFERENCES "pacientes" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION
);
-- liquibase formatted sql

-- changeset lucas:1761085819566-1
CREATE TABLE "usuarios" ("id" UUID NOT NULL, "cargo" VARCHAR(255) NOT NULL, "senha" VARCHAR(255) NOT NULL, "username" VARCHAR(255) NOT NULL, CONSTRAINT "usuarios_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-2
CREATE TABLE "pacientes" ("data_de_cadastro" date NOT NULL, "data_de_emissao" date, "data_de_nascimento" date NOT NULL, "is_aluno" BOOLEAN NOT NULL, "endereco_id" UUID, "id" UUID NOT NULL, "responsavel_id" UUID, "alergias" VARCHAR(255), "book" VARCHAR(255), "cartorio" VARCHAR(255), "cns" VARCHAR(255), "contato" VARCHAR(255), "cpf" VARCHAR(255), "fls" VARCHAR(255), "naturalidade" VARCHAR(255), "nis" VARCHAR(255), "nome_completo" VARCHAR(255) NOT NULL, "numero_registro_de_nascimento" VARCHAR(255), "orgao_emissor" VARCHAR(255), "rg" VARCHAR(255), CONSTRAINT "pacientes_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-3
CREATE TABLE "profissionais_da_saude" ("endereco_id" UUID, "id" UUID NOT NULL, "area_da_saude" VARCHAR(255), "contato" VARCHAR(255), "documento_profisisonal" VARCHAR(255), "email" VARCHAR(255), "nome" VARCHAR(255), "rg" VARCHAR(255), CONSTRAINT "profissionais_da_saude_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-4
ALTER TABLE "usuarios" ADD CONSTRAINT "usuarios_username_key" UNIQUE ("username");

-- changeset lucas:1761085819566-5
ALTER TABLE "pacientes" ADD CONSTRAINT "pacientes_endereco_id_key" UNIQUE ("endereco_id");

-- changeset lucas:1761085819566-6
ALTER TABLE "pacientes" ADD CONSTRAINT "pacientes_responsavel_id_key" UNIQUE ("responsavel_id");

-- changeset lucas:1761085819566-7
ALTER TABLE "profissionais_da_saude" ADD CONSTRAINT "profissionais_da_saude_endereco_id_key" UNIQUE ("endereco_id");

-- changeset lucas:1761085819566-8
CREATE TABLE "agendamentos" ("confirmado" BOOLEAN NOT NULL, "frequencia_dias" INTEGER NOT NULL, "hora_proxima_consulta" time(6) WITHOUT TIME ZONE NOT NULL, "proxima_consulta" date NOT NULL, "data_criacao" TIMESTAMP WITHOUT TIME ZONE, "id" UUID NOT NULL, "paciente_id" UUID NOT NULL, "profissional_id" UUID NOT NULL, "descricao" VARCHAR(255), "justificativa" VARCHAR(255), CONSTRAINT "agendamentos_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-9
CREATE TABLE "enderecos" ("id" UUID NOT NULL, "bairro" VARCHAR(255) NOT NULL, "cep" VARCHAR(255) NOT NULL, "cidade" VARCHAR(255) NOT NULL, "complemento" VARCHAR(255), "estado" VARCHAR(255) NOT NULL, "numero" VARCHAR(255) NOT NULL, "rua" VARCHAR(255) NOT NULL, CONSTRAINT "enderecos_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-10
CREATE TABLE "historico_de_consultas" ("data_consulta" date NOT NULL, "foi_realizada" BOOLEAN NOT NULL, "hora_consulta" time(6) WITHOUT TIME ZONE NOT NULL, "data_criacao" TIMESTAMP WITHOUT TIME ZONE NOT NULL, "agendamento_id" UUID NOT NULL, "id" UUID NOT NULL, "justificativa" VARCHAR(255), CONSTRAINT "historico_de_consultas_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-11
CREATE TABLE "pais" ("vivo" BOOLEAN NOT NULL, "id" UUID NOT NULL, "paciente_id" UUID NOT NULL, "cpf" VARCHAR(255), "nome" VARCHAR(255) NOT NULL, "parentesco" VARCHAR(255) NOT NULL, "profissao" VARCHAR(255), "rg" VARCHAR(255), CONSTRAINT "pais_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-12
CREATE TABLE "responsaveis" ("id" UUID NOT NULL, "contato" VARCHAR(255) NOT NULL, "nome" VARCHAR(255) NOT NULL, "parentesco" VARCHAR(255) NOT NULL, CONSTRAINT "responsaveis_pkey" PRIMARY KEY ("id"));

-- changeset lucas:1761085819566-13
ALTER TABLE "pacientes" ADD CONSTRAINT "FKfu04l1g96g6xprmpqgubp2j4k" FOREIGN KEY ("responsavel_id") REFERENCES "responsaveis" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset lucas:1761085819566-14
ALTER TABLE "pais" ADD CONSTRAINT "FKl1kpt66v4p19fvo4tf1pl6vmp" FOREIGN KEY ("paciente_id") REFERENCES "pacientes" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset lucas:1761085819566-15
ALTER TABLE "pacientes" ADD CONSTRAINT "FKob6a33a783gd5o3wqxvs85ukm" FOREIGN KEY ("endereco_id") REFERENCES "enderecos" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;

-- changeset lucas:1761085819566-16
ALTER TABLE "profissionais_da_saude" ADD CONSTRAINT "FKos8pekhrkun964qbarc4ib2db" FOREIGN KEY ("endereco_id") REFERENCES "enderecos" ("id") ON UPDATE NO ACTION ON DELETE NO ACTION;


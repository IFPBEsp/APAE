CREATE TABLE documentos_digitalizados (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome_bucket VARCHAR(255) NOT NULL,
    paciente_id BIGINT NOT NULL,
    nome_paciente VARCHAR(255) NOT NULL,
    tipo_paciente VARCHAR(255) NOT NULL,
    tipo_documento VARCHAR(255) NOT NULL,
    documento VARCHAR(255) NOT NULL,
    data_atualizacao TIMESTAMP NOT NULL
);
CREATE TABLE tb_laudo_medico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    paciente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_emissao TIMESTAMP,
    descricao TEXT
);
CREATE TABLE documentos_digitalizados (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          paciente_id BIGINT NOT NULL,
                                          tipo_de_documento VARCHAR(255) NOT NULL,
                                          documento VARCHAR(255) NOT NULL UNIQUE
);
CREATE TABLE documentos_digitalizados (
                                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                          paciente_id BIGINT NOT NULL,
                                          encaminhamento UUID NOT NULL UNIQUE,
                                          laudo_medico UUID NOT NULL UNIQUE
);
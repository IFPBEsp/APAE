CREATE TABLE documentos_escolares (
    id UUID PRIMARY KEY,
    paciente_id UUID NOT NULL,
    ano INT NOT NULL,
    nome_arquivo VARCHAR(255) NOT NULL,
    caminho_arquivo VARCHAR(255) NOT NULL,
    data_upload TIMESTAMP NOT NULL
);

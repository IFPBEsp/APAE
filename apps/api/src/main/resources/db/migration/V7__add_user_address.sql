ALTER TABLE apae_geral.usuarios
    ADD COLUMN endereco_id UUID;

ALTER TABLE apae_geral.usuarios
    ADD CONSTRAINT uk_usuarios_endereco UNIQUE (endereco_id);

ALTER TABLE apae_geral.usuarios
    ADD CONSTRAINT fk_usuarios_endereco
        FOREIGN KEY (endereco_id)
        REFERENCES apae_geral.enderecos (id)
        ON UPDATE CASCADE;

SET search_path TO apae_geral;

ALTER TABLE usuarios
    ALTER COLUMN cpf DROP NOT NULL,
    ALTER COLUMN senha DROP NOT NULL,
    ADD COLUMN contato VARCHAR(255),
    ADD COLUMN rg VARCHAR(255),
    ADD CONSTRAINT uk_usuarios_rg UNIQUE (rg);

ALTER TABLE profissionais_da_saude
    ADD COLUMN usuario_id UUID;

UPDATE usuarios u
SET nome_completo = COALESCE(u.nome_completo, p.nome),
    contato = COALESCE(u.contato, p.contato),
    rg = COALESCE(u.rg, p.rg),
    cargo = CASE
        WHEN u.cargo = 'ADMIN' THEN u.cargo
        ELSE 'APAE_GERAL'
    END
FROM profissionais_da_saude p
WHERE lower(u.email) = lower(p.email);

UPDATE profissionais_da_saude p
SET usuario_id = u.id
FROM usuarios u
WHERE lower(u.email) = lower(p.email);

INSERT INTO usuarios (id, email, cpf, senha, nome_completo, cargo, contato, rg)
SELECT p.id,
       p.email,
       NULL,
       NULL,
       p.nome,
       'APAE_GERAL',
       p.contato,
       p.rg
FROM profissionais_da_saude p
WHERE p.usuario_id IS NULL;

UPDATE profissionais_da_saude p
SET usuario_id = u.id
FROM usuarios u
WHERE p.usuario_id IS NULL
  AND lower(u.email) = lower(p.email);

ALTER TABLE profissionais_da_saude
    ALTER COLUMN usuario_id SET NOT NULL,
    ADD CONSTRAINT uk_profissionais_da_saude_usuario UNIQUE (usuario_id),
    ADD CONSTRAINT fk_profissionais_da_saude_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
        ON UPDATE CASCADE;

ALTER TABLE profissionais_da_saude
    DROP CONSTRAINT IF EXISTS uk_profissionais_da_saude_email,
    DROP CONSTRAINT IF EXISTS uk_profissionais_da_saude_rg,
    DROP CONSTRAINT IF EXISTS uk_profissionais_da_saude_endereco,
    DROP CONSTRAINT IF EXISTS fk_profissionais_da_saude_endereco;

ALTER TABLE profissionais_da_saude
    DROP COLUMN nome,
    DROP COLUMN email,
    DROP COLUMN contato,
    DROP COLUMN rg,
    DROP COLUMN endereco_id;

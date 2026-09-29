ALTER TABLE apae_geral.profissionais_da_saude
    ADD COLUMN IF NOT EXISTS foto_perfil_nome VARCHAR(255),
    ADD COLUMN IF NOT EXISTS foto_perfil_ano INTEGER;

ALTER TABLE apae_geral.usuarios
    -- teste do bloqueio de migration existente
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;
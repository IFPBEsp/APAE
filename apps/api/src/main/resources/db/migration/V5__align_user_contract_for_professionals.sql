SET search_path TO apae_geral;

ALTER TABLE usuarios
    ALTER COLUMN cpf DROP NOT NULL,
    ALTER COLUMN senha DROP NOT NULL,
    ADD COLUMN IF NOT EXISTS contato VARCHAR(255),
    ADD COLUMN IF NOT EXISTS rg VARCHAR(255);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_usuarios_rg'
          AND conrelid = 'apae_geral.usuarios'::regclass
    ) THEN
        ALTER TABLE apae_geral.usuarios
            ADD CONSTRAINT uk_usuarios_rg UNIQUE (rg);
    END IF;
END $$;

COMMENT ON COLUMN usuarios.cpf IS
    'CPF e obrigatorio para usuarios cadastrados pelo fluxo de autenticacao; profissionais migrados podem permanecer sem CPF ate atualizacao cadastral.';

COMMENT ON COLUMN usuarios.senha IS
    'Senha nula indica profissional migrado ou criado sem credencial inicial; o acesso deve ser ativado por redefinicao de senha.';

COMMENT ON COLUMN usuarios.contato IS
    'Contato centralizado do usuario, incluindo profissionais da saude migrados de profissionais_da_saude.contato.';

COMMENT ON COLUMN usuarios.rg IS
    'Documento de identidade centralizado do usuario, incluindo profissionais da saude migrados de profissionais_da_saude.rg.';

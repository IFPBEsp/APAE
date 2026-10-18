ALTER TABLE apae_geral.usuarios
    ADD COLUMN IF NOT EXISTS primeiro_acesso BOOLEAN NOT NULL DEFAULT FALSE;

COMMENT ON COLUMN apae_geral.usuarios.primeiro_acesso IS
    'Indica que o usuario de atendimento deve definir senha no primeiro acesso.';

UPDATE apae_geral.usuarios u
SET primeiro_acesso = TRUE
FROM apae_geral.profissionais_da_saude p
WHERE p.usuario_id = u.id
  AND u.cargo = 'ATENDIMENTO'
  AND u.senha IS NULL
  AND u.cpf IS NOT NULL;

CREATE OR REPLACE FUNCTION apae_geral.definir_senha_primeiro_acesso(
    p_usuario_id UUID,
    p_senha_hash TEXT
)
RETURNS VOID
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path = apae_geral, pg_temp
AS $$
BEGIN
    IF p_usuario_id IS NULL THEN
        RAISE EXCEPTION 'Usuario invalido';
    END IF;

    IF p_senha_hash IS NULL OR length(trim(p_senha_hash)) = 0 THEN
        RAISE EXCEPTION 'Senha invalida';
    END IF;

    UPDATE usuarios
    SET senha = p_senha_hash,
        primeiro_acesso = FALSE
    WHERE id = p_usuario_id
      AND primeiro_acesso = TRUE
      AND cargo = 'ATENDIMENTO';

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Usuario nao encontrado ou primeiro acesso ja concluido';
    END IF;
END;
$$;

REVOKE ALL ON FUNCTION apae_geral.definir_senha_primeiro_acesso(UUID, TEXT) FROM PUBLIC;

SET search_path TO apae_geral;

DO $$
DECLARE
    constraint_name TEXT;
BEGIN
    FOR constraint_name IN
        SELECT c.conname
        FROM pg_constraint c
        JOIN pg_class t ON t.oid = c.conrelid
        JOIN pg_namespace n ON n.oid = t.relnamespace
        WHERE n.nspname = 'apae_geral'
          AND t.relname = 'usuarios'
          AND c.contype = 'c'
          AND pg_get_constraintdef(c.oid) ILIKE '%cargo%'
    LOOP
        EXECUTE format('ALTER TABLE apae_geral.usuarios DROP CONSTRAINT %I', constraint_name);
    END LOOP;
END $$;

UPDATE usuarios
SET cargo = 'APAE_GERAL'
WHERE cargo = 'USER';

ALTER TABLE usuarios
    ADD CONSTRAINT chk_usuarios_cargo
    CHECK (cargo IN ('ADMIN', 'GESTAO_ESCOLAR', 'APAE_GERAL', 'ATENDIMENTO'));

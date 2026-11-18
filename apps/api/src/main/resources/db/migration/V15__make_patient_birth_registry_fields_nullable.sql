SET search_path TO apae_geral;

ALTER TABLE pacientes
    ALTER COLUMN cartorio DROP NOT NULL,
    ALTER COLUMN fls DROP NOT NULL,
    ALTER COLUMN livro DROP NOT NULL;

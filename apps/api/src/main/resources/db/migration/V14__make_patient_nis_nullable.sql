SET search_path TO apae_geral;

ALTER TABLE pacientes
    ALTER COLUMN nis DROP NOT NULL;

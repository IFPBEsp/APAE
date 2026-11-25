SET search_path TO apae_geral;

ALTER TABLE cadastros_anuais ALTER COLUMN doencas DROP NOT NULL;

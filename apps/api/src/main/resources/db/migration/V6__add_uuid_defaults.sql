ALTER TABLE apae_geral.usuarios
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.password_recovery_token
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.enderecos
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.profissionais_da_saude
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.disponibilidades
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.pacientes
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.parentes
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.responsaveis
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.transtornos
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.vacinas
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.cadastros_anuais
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.agendamentos
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.agendamento_gerado
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE apae_geral.falta
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

BEGIN;

-- Dados exclusivamente fictícios para desenvolvimento local.
-- Senha do administrador: 123456

INSERT INTO apae_geral.areas_de_atendimento (area)
VALUES
    ('Fisioterapia'),
    ('Fonoaudiologia'),
    ('Psicologia')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.enderecos
    (id, cidade, cep, estado, bairro, rua, numero, complemento)
VALUES
    ('10000000-0000-4000-8000-000000000001', 'Esperança', '58135-000', 'PB', 'Centro', 'Rua Local Um', '100', NULL),
    ('10000000-0000-4000-8000-000000000002', 'Esperança', '58135-000', 'PB', 'Centro', 'Rua Local Dois', '200', 'Casa'),
    ('10000000-0000-4000-8000-000000000003', 'Esperança', '58135-000', 'PB', 'Bela Vista', 'Rua Local Três', '300', NULL),
    ('10000000-0000-4000-8000-000000000004', 'Esperança', '58135-000', 'PB', 'Bela Vista', 'Rua Local Quatro', '400', NULL)
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.usuarios
    (id, email, cpf, senha, nome_completo, cargo, contato, rg, endereco_id, primeiro_acesso, ativo)
VALUES
    (
        '20000000-0000-4000-8000-000000000001',
        'admin@teste.local',
        '000.000.001-91',
        '$2a$10$a7iR65cTGffpfuaBAImdHegVl99oyUHk.w6ldu9YmpBGs7dhIpLtK',
        'Administrador Local',
        'APAE_GERAL',
        '(83) 90000-0001',
        'LOCAL-ADMIN-01',
        NULL,
        FALSE,
        TRUE
    ),
    (
        '20000000-0000-4000-8000-000000000002',
        'ana.profissional@teste.local',
        '000.000.002-72',
        NULL,
        'Ana Profissional Fictícia',
        'ATENDIMENTO',
        '(83) 90000-0002',
        'LOCAL-PROF-01',
        '10000000-0000-4000-8000-000000000001',
        TRUE,
        TRUE
    ),
    (
        '20000000-0000-4000-8000-000000000003',
        'bruno.profissional@teste.local',
        '000.000.003-53',
        NULL,
        'Bruno Profissional Fictício',
        'ATENDIMENTO',
        '(83) 90000-0003',
        'LOCAL-PROF-02',
        '10000000-0000-4000-8000-000000000002',
        TRUE,
        TRUE
    )
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.profissionais_da_saude
    (id, area_de_atendimento, documento_profissional, ativo, foto_perfil, usuario_id)
VALUES
    (
        '30000000-0000-4000-8000-000000000001',
        'Psicologia',
        'CRP-LOCAL-001',
        TRUE,
        NULL,
        '20000000-0000-4000-8000-000000000002'
    ),
    (
        '30000000-0000-4000-8000-000000000002',
        'Fisioterapia',
        'CREFITO-LOCAL-001',
        TRUE,
        NULL,
        '20000000-0000-4000-8000-000000000003'
    )
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.disponibilidades (id, day, shift, professional_id)
VALUES
    ('31000000-0000-4000-8000-000000000001', 'SEGUNDA', 'MANHA', '30000000-0000-4000-8000-000000000001'),
    ('31000000-0000-4000-8000-000000000002', 'QUARTA', 'TARDE', '30000000-0000-4000-8000-000000000001'),
    ('31000000-0000-4000-8000-000000000003', 'TERCA', 'MANHA', '30000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.pacientes
    (id, nome_completo, naturalidade, data_de_nascimento, contato,
     numero_registro_de_nascimento, cartorio, fls, livro, rg,
     data_de_emissao, orgao_emissor, cpf, cns, nis, data_de_cadastro,
     alergias, is_aluno, is_apagado, endereco_id)
VALUES
    (
        '40000000-0000-4000-8000-000000000001',
        'João Paciente Fictício', 'Esperança - PB', DATE '2012-04-10', '(83) 90000-1001',
        'REG-LOCAL-001', 'Cartório Local', '10', 'A-01', 'RG-LOCAL-PAC-01',
        DATE '2020-01-10', 'SSP-PB', '000.000.101-91', 'CNS-LOCAL-001', 'NIS-LOCAL-001', CURRENT_DATE,
        'Nenhuma', TRUE, FALSE, '10000000-0000-4000-8000-000000000003'
    ),
    (
        '40000000-0000-4000-8000-000000000002',
        'Maria Paciente Fictícia', 'Esperança - PB', DATE '2015-08-21', '(83) 90000-1002',
        'REG-LOCAL-002', 'Cartório Local', '20', 'A-02', 'RG-LOCAL-PAC-02',
        DATE '2021-02-15', 'SSP-PB', '000.000.102-72', 'CNS-LOCAL-002', 'NIS-LOCAL-002', CURRENT_DATE,
        'Lactose', FALSE, FALSE, '10000000-0000-4000-8000-000000000004'
    )
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.responsaveis
    (id, nome, contato, parentesco, endereco_id, paciente_id)
VALUES
    ('41000000-0000-4000-8000-000000000001', 'Responsável Fictício Um', '(83) 90000-2001', 'Mãe', NULL, '40000000-0000-4000-8000-000000000001'),
    ('41000000-0000-4000-8000-000000000002', 'Responsável Fictício Dois', '(83) 90000-2002', 'Pai', NULL, '40000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.parentes
    (id, nome, rg, cpf, vivo, profissao, parentesco, paciente_id)
VALUES
    ('42000000-0000-4000-8000-000000000001', 'Parente Fictício Um', 'RG-LOCAL-PAR-01', '000.000.201-91', TRUE, 'Autônoma', 'Mãe', '40000000-0000-4000-8000-000000000001'),
    ('42000000-0000-4000-8000-000000000002', 'Parente Fictício Dois', 'RG-LOCAL-PAR-02', '000.000.202-72', TRUE, 'Comerciante', 'Pai', '40000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.transtornos (id, nome)
VALUES
    ('50000000-0000-4000-8000-000000000001', 'Transtorno fictício para testes'),
    ('50000000-0000-4000-8000-000000000002', 'Deficiência fictícia para testes')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.vacinas (id, nome)
VALUES
    ('51000000-0000-4000-8000-000000000001', 'Vacina fictícia A'),
    ('51000000-0000-4000-8000-000000000002', 'Vacina fictícia B')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.paciente_vacina (paciente_id, vacina_id)
VALUES
    ('40000000-0000-4000-8000-000000000001', '51000000-0000-4000-8000-000000000001'),
    ('40000000-0000-4000-8000-000000000002', '51000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.cadastros_anuais
    (id, bpc, doencas, medicamentos_continuos, renda_familiar, ano, paciente_id)
VALUES
    ('60000000-0000-4000-8000-000000000001', 'Não', 'Nenhuma', NULL, 1800.00, EXTRACT(YEAR FROM CURRENT_DATE)::INTEGER, '40000000-0000-4000-8000-000000000001'),
    ('60000000-0000-4000-8000-000000000002', 'Sim', 'Nenhuma', 'Medicamento fictício', 2200.00, EXTRACT(YEAR FROM CURRENT_DATE)::INTEGER, '40000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.cadastro_anual_transtorno (cadastro_anual_id, transtorno_id)
VALUES
    ('60000000-0000-4000-8000-000000000001', '50000000-0000-4000-8000-000000000001'),
    ('60000000-0000-4000-8000-000000000002', '50000000-0000-4000-8000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.cadastro_anual_areas_de_atendimento
    (cadastro_anual_id, areas_de_atendimento_id)
SELECT '60000000-0000-4000-8000-000000000001'::UUID, id
FROM apae_geral.areas_de_atendimento
WHERE area = 'Psicologia'
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.cadastro_anual_areas_de_atendimento
    (cadastro_anual_id, areas_de_atendimento_id)
SELECT '60000000-0000-4000-8000-000000000002'::UUID, id
FROM apae_geral.areas_de_atendimento
WHERE area = 'Fisioterapia'
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.agendamentos
    (id, profissional_id, cadastro_anual_id, frequencia_dias, hora,
     data_inicial, data_final, ativo, data_criacao)
VALUES
    (
        '70000000-0000-4000-8000-000000000001',
        '30000000-0000-4000-8000-000000000001',
        '60000000-0000-4000-8000-000000000001',
        7, TIME '09:00', CURRENT_DATE, CURRENT_DATE + 90, TRUE, CURRENT_TIMESTAMP
    ),
    (
        '70000000-0000-4000-8000-000000000002',
        '30000000-0000-4000-8000-000000000002',
        '60000000-0000-4000-8000-000000000002',
        14, TIME '14:00', CURRENT_DATE, CURRENT_DATE + 90, TRUE, CURRENT_TIMESTAMP
    )
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.agendamento_gerado
    (id, agendamento_id, data_hora_agendada, realizada, cancelada, paciente_id)
VALUES
    (
        '71000000-0000-4000-8000-000000000001',
        '70000000-0000-4000-8000-000000000001',
        CURRENT_DATE + TIME '09:00', FALSE, FALSE,
        '40000000-0000-4000-8000-000000000001'
    ),
    (
        '71000000-0000-4000-8000-000000000002',
        '70000000-0000-4000-8000-000000000002',
        CURRENT_DATE + TIME '14:00', FALSE, FALSE,
        '40000000-0000-4000-8000-000000000002'
    )
ON CONFLICT DO NOTHING;

INSERT INTO apae_geral.falta
    (id, agendamento_gerado_id, data_falta, justificativa, notificado, is_justificada)
VALUES
    (
        '72000000-0000-4000-8000-000000000001',
        '71000000-0000-4000-8000-000000000002',
        CURRENT_DATE,
        'Falta fictícia para validação da tela local',
        FALSE,
        FALSE
    )
ON CONFLICT DO NOTHING;

COMMIT;

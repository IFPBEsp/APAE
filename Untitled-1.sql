SELECT * FROM view_avaliacoes_escolares;
SELECT * FROM view_relatorios_escolares;

INSERT INTO pacientes (
    id, nome_completo, naturalidade, data_de_nascimento, contato, 
    numero_registro_de_nascimento, cartorio, fls, livro, rg, 
    data_de_emissao, orgao_emissor, cpf, cns, nis, 
    data_de_cadastro, alergias, is_aluno, is_apagado
) VALUES (
    '10dbd3f5-01de-4c24-88b7-0923fc55a47d', 
    'João da Silva Mock', 
    'João Pessoa', 
    '2015-05-10', 
    '(83) 99999-8888', 
    '123456', 
    '1º Cartório', 
    '100', 
    'A-12', 
    '12.345.678-9', 
    '2020-01-01', 
    'SSP', 
    '123.456.789-01', 
    '123456789012345', 
    'NIS-123', 
    '2024-04-24', 
    'Nenhuma', 
    true, 
    false
) ON CONFLICT (id) DO NOTHING;

INSERT INTO avaliacoes_view (
    id, 
    aluno_id, 
    descricao_avaliacao, 
    data_avaliacao, 
    nome_professor
) VALUES (
    1, 
    '10dbd3f5-01de-4c24-88b7-0923fc55a47d', 
    'O aluno João demonstrou excelente coordenação motora nas atividades de teste.', 
    now(), 
    'Professor Pedro Alcantara'
) ON CONFLICT (id) DO NOTHING;
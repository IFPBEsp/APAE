SELECT * FROM "pacientes" WHERE id = 'c4e8f1d2-a7b3-4e5a-8c9d-1f2e3a4b5c6d';
SELECT * FROM "view_avaliacoes_escolares" WHERE aluno_id = 'c4e8f1d2-a7b3-4e5a-8c9d-1f2e3a4b5c6d';

INSERT INTO "view_avaliacoes_escolares" (
    id,
    descricao, 
    data_avaliacao, 
    aluno_id, 
    professor_nome
) VALUES (
    1, -- O ID aqui é Long (numérico)
    'O aluno demonstrou excelente autonomia e participação nas atividades em grupo.', 
    now(), 
    'c4e8f1d2-a7b3-4e5a-8c9d-1f2e3a4b5c6d', -- O UUID do paciente que já existe
    'Professor Carlos Eduardo'
) ON CONFLICT (id) DO NOTHING;
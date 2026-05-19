CREATE TABLE IF NOT EXISTS pacientes (
    id UUID PRIMARY KEY,
    nome_completo VARCHAR(255),
    is_aluno BOOLEAN,
    is_apagado BOOLEAN,
    data_de_cadastro TIMESTAMP
);

DROP VIEW IF EXISTS view_relatorios_escolares CASCADE;
DROP VIEW IF EXISTS view_avaliacoes_escolares CASCADE;

CREATE VIEW view_relatorios_escolares AS
SELECT
    md5(p.id::text || 'relatorio')::uuid AS relatorio_id,
    p.id AS aluno_id,
    p.nome_completo AS aluno_nome,
    '22222222-2222-2222-2222-222222222222'::uuid AS professor_id,
    'Professor Teste Escolar'::varchar(255) AS professor_nome,
    '33333333-3333-3333-3333-333333333333'::uuid AS turma_id,
    'Turma Teste Escolar A'::varchar(255) AS turma_nome,
    'Atividades de leitura, escrita básica e reconhecimento de letras.'::text AS atividades,
    'Coordenação motora fina, atenção e reconhecimento de padrões.'::text AS habilidades,
    'Repetição guiada, estímulo visual e reforço positivo.'::text AS estrategias,
    'Cartões ilustrados, caderno pedagógico e lápis adaptado.'::text AS recursos,
    CURRENT_TIMESTAMP AS created_at
FROM pacientes p
WHERE p.is_aluno = true
  AND p.is_apagado = false;

CREATE VIEW view_avaliacoes_escolares AS
SELECT
    md5(p.id::text || 'avaliacao')::uuid AS avaliacao_id,
    p.id AS aluno_id,
    p.nome_completo AS aluno_nome,
    '22222222-2222-2222-2222-222222222222'::uuid AS professor_id,
    'Professor Teste Escolar'::varchar(255) AS professor_nome,
    'O aluno apresentou evolução significativa nas atividades propostas, demonstrando maior autonomia e participação nas tarefas escolares.'::text AS descricao,
    CURRENT_DATE AS data_avaliacao
FROM pacientes p
WHERE p.is_aluno = true
  AND p.is_apagado = false;
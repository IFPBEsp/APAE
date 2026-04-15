DROP VIEW IF EXISTS view_relatorios_escolares CASCADE;
DROP VIEW IF EXISTS view_avaliacoes_escolares CASCADE;

CREATE VIEW view_relatorios_escolares AS
WITH aluno_base AS (
    SELECT p.id, p.nome_completo
    FROM pacientes p
    WHERE p.is_aluno = true
      AND p.is_apagado = false
    ORDER BY p.data_de_cadastro ASC, p.nome_completo ASC
    LIMIT 1
)
SELECT
    '11111111-1111-1111-1111-111111111111'::uuid AS relatorio_id,
    a.id AS aluno_id,
    a.nome_completo AS aluno_nome,
    '22222222-2222-2222-2222-222222222222'::uuid AS professor_id,
    'Professor Teste Escolar'::varchar(255) AS professor_nome,
    '33333333-3333-3333-3333-333333333333'::uuid AS turma_id,
    'Turma Teste Escolar A'::varchar(255) AS turma_nome,
    'Atividades de leitura, escrita básica e reconhecimento de letras.'::text AS atividades,
    'Coordenação motora fina, atenção e reconhecimento de padrões.'::text AS habilidades,
    'Repetição guiada, estímulo visual e reforço positivo.'::text AS estrategias,
    'Cartões ilustrados, caderno pedagógico e lápis adaptado.'::text AS recursos,
    CURRENT_TIMESTAMP AS created_at
FROM aluno_base a;

CREATE VIEW view_avaliacoes_escolares AS
WITH aluno_base AS (
    SELECT p.id, p.nome_completo
    FROM pacientes p
    WHERE p.is_aluno = true
      AND p.is_apagado = false
    ORDER BY p.data_de_cadastro ASC, p.nome_completo ASC
    LIMIT 1
)
SELECT
    '44444444-4444-4444-4444-444444444444'::uuid AS avaliacao_id,
    a.id AS aluno_id,
    a.nome_completo AS aluno_nome,
    '22222222-2222-2222-2222-222222222222'::uuid AS professor_id,
    'Professor Teste Escolar'::varchar(255) AS professor_nome,
    'O aluno apresentou evolução significativa nas atividades propostas, demonstrando maior autonomia e participação nas tarefas escolares.'::text AS descricao,
    CURRENT_DATE AS data_avaliacao
FROM aluno_base a;
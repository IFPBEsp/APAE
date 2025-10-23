CREATE OR REPLACE VIEW "agendamentos_gerados" AS
SELECT
    a.id AS agendamento_id,
    gs.data_consulta::date,
    a.hora,
    a.atendimento_id,
    a.cadastro_anual_id,
    ca.ano AS ano_cadastro,
    p.nome_completo AS paciente_nome
FROM
    agendamentos a
    JOIN cadastro_anual ca ON a.cadastro_anual_id = ca.id AND ca.ativo = TRUE
    JOIN pacientes p ON ca.paciente_id = p.id
CROSS JOIN LATERAL
    generate_series(
        a.data_inicial::timestamp,
        a.data_final::timestamp,
        a.frequencia_dias * interval '1 day'
    ) AS gs(data_consulta)
WHERE
    EXTRACT(YEAR FROM gs.data_consulta) = ca.ano;
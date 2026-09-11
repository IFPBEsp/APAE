UPDATE apae_geral.usuarios u
SET cargo = 'ATENDIMENTO'
FROM apae_geral.profissionais_da_saude p
WHERE p.usuario_id = u.id
  AND u.cargo = 'APAE_GERAL';

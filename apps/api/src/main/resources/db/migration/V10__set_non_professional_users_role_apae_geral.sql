UPDATE apae_geral.usuarios u
SET cargo = 'APAE_GERAL',
    primeiro_acesso = FALSE
WHERE u.cargo = 'ATENDIMENTO'
  AND NOT EXISTS (
      SELECT 1
      FROM apae_geral.profissionais_da_saude p
      WHERE p.usuario_id = u.id
  );

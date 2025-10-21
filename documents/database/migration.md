## Passos para Rodar Migrações

1. **Validar o Changelog**
   Certifique-se de que os arquivos de migração estão corretos:
   ```bash
   mvn liquibase:validate
   ```

2. **Aplicar as Migrações**
   Execute as migrações para atualizar o banco de dados:
   ```bash
   mvn liquibase:update
   ```

## Criar uma Nova Migração
   Adicione uma nova migração ao projeto:
   - Crie um arquivo SQL em `src/main/resources/db/changelog/changes/`, ex.: `002-add-column.sql`:
     ```sql
     -- liquibase formatted sql

     -- changeset seu_nome:2
     ALTER TABLE users ADD COLUMN phone_number VARCHAR(20);
     ```
   - Atualize o `db.changelog-master.yaml` em `src/main/resources/db/changelog/`:
     ```yaml
     databaseChangeLog:
       - include:
           file: changes/001-initial-schema.sql
       - include:
           file: changes/002-add-column.sql
     ```
   - Valide e aplique a nova migração:
     ```bash
     mvn liquibase:validate
     mvn liquibase:update
     ```
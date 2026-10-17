/**
 * Tamanho máximo permitido para uploads de arquivo.
 * Deve estar alinhado com `spring.servlet.multipart.max-file-size` no backend.
 */
export const MAX_FILE_SIZE_MB = 10;
export const MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024;
export const MAX_FILE_SIZE_LABEL = `${MAX_FILE_SIZE_MB}MB`;

package br.org.apae.documentos_digitalizados.global;

import java.time.Instant;

public record StandardError(
    Instant timestamp,
    Integer status,
    String error,
    String message,
    String path
) {}

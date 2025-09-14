plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "api"
include(
    "api-crud-pacientes",
    "auth-service",
    "documentos-digitalizados",
    "profissional-da-saude",
)

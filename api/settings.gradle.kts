plugins {
    // Apply the foojay-resolver plugin to allow automatic download of JDKs
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "api"
include(
    "demo",
    "auth-service", 
    "documentos-digitalizados", 
    "documentos-medicos-digitalizados", 
    "documentos-pessoais-digitalizados", 
    "api-crud-pacientes", 
    "documentos-escolares"
)

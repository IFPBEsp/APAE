package br.org.apae.documentos_pessoais_digitalizados.application.exception;

public class FileExistException extends RuntimeException {
    public FileExistException(String fileName, String patientId) {
        super("O arquivo " + fileName + " já existe para o paciente: " + patientId);
    }
}

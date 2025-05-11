package br.org.apae.documentos_medicos.domain.models;

import java.time.LocalDate;
import java.util.UUID;

public class MedicalDocument {

    private UUID id;
    private String nomeArquivo;  
    private MedcialDocumentType tipoDocumento;
    private String descricao;
    private LocalDate dataReferencia;
    private LocalDate dataUpload;
    private String caminhoBucket;
    private Boolean ativo = true;

    public MedicalDocument(UUID id, String nomeArquivo, MedcialDocumentType tipoDocumento, String descricao,
            LocalDate dataReferencia, LocalDate dataUpload, String caminhoBucket, Boolean ativo) {
        this.id = id;
        this.nomeArquivo = nomeArquivo;
        this.tipoDocumento = tipoDocumento;
        this.descricao = descricao;
        this.dataReferencia = dataReferencia;
        this.dataUpload = dataUpload;
        this.caminhoBucket = caminhoBucket;
        this.ativo = ativo;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public MedcialDocumentType getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(MedcialDocumentType tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public LocalDate getDataUpload() {
        return dataUpload;
    }

    public void setDataUpload(LocalDate dataUpload) {
        this.dataUpload = dataUpload;
    }

    public String getCaminhoBucket() {
        return caminhoBucket;
    }

    public void setCaminhoBucket(String caminhoBucket) {
        this.caminhoBucket = caminhoBucket;
    }

}
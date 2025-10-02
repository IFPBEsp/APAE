package br.org.apae.api.professional.validations;

public final class RegexPatterns {
    private RegexPatterns() {}

    public static final String TELEFONE = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";
    public static final String DOC_PROFISSIONAL = "^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\\s\\d{2})?\\s?\\d{1,6}$";
    public static final String NOME = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$";
    public static final String RG = "^("
            + "\\d\\.\\d{3}\\.\\d{3}" + "|\\d{7,8}[\\dXx]?" + "|\\d{2}\\.\\d{3}\\.\\d{3}-[\\dXx]" + ")$";
    public static final String ESTADO = "^[A-Z]{2}$";
    public static final String CIDADE = "^[A-Za-zÀ-ÿ\\s]+$";
    public static final String BAIRRO = "^[A-Za-zÀ-ÿ\\s]+$";
    public static final String RUA = "^[A-Za-zÀ-ÿ0-9\\s]+$";
    public static final String NUMERO = "^\\d+[A-Za-z]?$";
    public static final String CEP = "^\\d{5}-\\d{3}$";
    public static final String COMPLEMENTO = "^[A-Za-zÀ-ÿ0-9\\s\\-\\/\\.]*$";
}
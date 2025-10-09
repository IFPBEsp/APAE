package br.org.apae.api.common.validations;

public final class RegexPatterns {
    private RegexPatterns() {}

    public static final String PHONE_NUMBER = "^\\(\\d{2}\\) \\d{5}-\\d{4}$";
    public static final String PROFESSIONAL_DOCUMENT = "^(CRM|COREN|CREFITO|CRFa|CRP|CRESS)([-/][A-Z0-9]{1,2}|\\s\\d{2})?\\s?\\d{1,6}$";
    public static final String NAME = "^[A-Za-zÀ-ÖØ-öø-ÿ ]{3,100}$";
    public static final String IDENTITY_DOCUMENT_RG = "^("
            + "\\d\\.\\d{3}\\.\\d{3}" + "|\\d{7,8}[\\dXx]?" + "|\\d{2}\\.\\d{3}\\.\\d{3}-[\\dXx]" + ")$";
    public static final String STATE_PROVINCE = "^[A-Z]{2}$";
    public static final String CITY = "^[A-Za-zÀ-ÿ\\s]+$";
    public static final String NEIGHBORHOOD = "^[A-Za-zÀ-ÿ\\s]+$";
    public static final String STREET = "^[A-Za-zÀ-ÿ0-9\\s]+$";
    public static final String NUMBER = "^\\d+[A-Za-z]?$";
    public static final String ZIP_CODE_CEP = "^\\d{5}-\\d{3}$";
    public static final String COMPLEMENT = "^[A-Za-zÀ-ÿ0-9\\s\\-\\/\\.]*$";
}
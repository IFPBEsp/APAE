package br.org.apae.api.common.validations;

public final class ValidationMessages {
    private ValidationMessages() {}

    public static final String INVALID_PHONE = "Invalid phone number. Expected format: (xx) xxxxx-xxxx";
    public static final String INVALID_PROFESSIONAL_DOCUMENT = "Invalid professional document";
    public static final String INVALID_EMAIL = "Invalid email address";
    public static final String INVALID_NAME = "Invalid name";
    public static final String INVALID_IDENTITY_DOCUMENT = "Invalid RG (Identity Document)";
    public static final String INVALID_STATE = "Invalid state/province. Expected format: XX";
    public static final String INVALID_CITY = "Invalid city. Cannot contain numbers";
    public static final String INVALID_NEIGHBORHOOD = "Invalid neighborhood";
    public static final String INVALID_STREET = "Invalid street";
    public static final String INVALID_NUMBER = "Invalid number";
    public static final String INVALID_ZIP_CODE = "Invalid ZIP code (CEP). Expected format: XXXXX-XXX";
    public static final String INVALID_COMPLEMENT = "Invalid complement/additional information";
}
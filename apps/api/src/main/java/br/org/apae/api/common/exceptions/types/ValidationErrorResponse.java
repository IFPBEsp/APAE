package br.org.apae.api.common.exceptions.types;

import java.util.List;

public class ValidationErrorResponse extends ErrorResponse {

    private final List<FieldError> fields;

    public ValidationErrorResponse(int status, String error, String message, String path, List<FieldError> fields) {
        super(status, error, message, path);
        this.fields = fields;
    }

    public List<FieldError> getFields() {
        return fields;
    }

    public static class FieldError {
        private final String field;
        private final String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
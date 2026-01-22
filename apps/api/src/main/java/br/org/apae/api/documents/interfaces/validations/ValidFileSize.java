package br.org.apae.api.documents.interfaces.validations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFileSize.Validator.class)
@Documented
public @interface ValidFileSize {
    String message() default "O tamanho do arquivo excede o limite máximo permitido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Tamanho máximo em bytes (padrão: 10MB)
     */
    long maxSize() default 10 * 1024 * 1024; // 10MB

    class Validator implements ConstraintValidator<ValidFileSize, List<MultipartFile>> {
        private long maxSize;

        @Override
        public void initialize(ValidFileSize annotation) {
            this.maxSize = annotation.maxSize();
        }

        @Override
        public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
            if (files == null || files.isEmpty()) {
                return true; // Deixar outras validações tratarem isso
            }

            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }

                long fileSize = file.getSize();
                if (fileSize > maxSize) {
                    context.disableDefaultConstraintViolation();
                    String maxSizeMB = String.format("%.2f", maxSize / (1024.0 * 1024.0));
                    String fileSizeMB = String.format("%.2f", fileSize / (1024.0 * 1024.0));
                    context.buildConstraintViolationWithTemplate(
                            String.format("O arquivo '%s' possui %.2f MB e excede o limite máximo de %s MB", 
                                    file.getOriginalFilename(), fileSizeMB, maxSizeMB))
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }
    }
}


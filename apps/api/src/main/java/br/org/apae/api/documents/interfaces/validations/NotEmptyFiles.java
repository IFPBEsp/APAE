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
@Constraint(validatedBy = NotEmptyFiles.Validator.class)
@Documented
public @interface NotEmptyFiles {
    String message() default "A lista de arquivos não pode estar vazia";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<NotEmptyFiles, List<MultipartFile>> {
        @Override
        public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
            if (files == null || files.isEmpty()) {
                return false;
            }
            return files.stream().anyMatch(file -> file != null && !file.isEmpty());
        }
    }
}

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
import java.util.Arrays;
import java.util.List;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ValidFileFormat.ListValidator.class, ValidFileFormat.SingleFileValidator.class })
@Documented
public @interface ValidFileFormat {
    String message() default "Formato de arquivo inválido. Formatos permitidos: PDF, JPG, PNG, DOCX, WEBP";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class ValidationHelper {
        static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
                "application/pdf",
                "image/jpeg",
                "image/jpg",
                "image/png",
                "image/webp",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
        );

        static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
                "pdf", "jpg", "jpeg", "png", "docx", "webp"
        );

        static boolean isValidFile(MultipartFile file, ConstraintValidatorContext context) {
            if (file == null || file.isEmpty()) {
                return true;
            }

            String contentType = file.getContentType();
            String originalFilename = file.getOriginalFilename();

            if (contentType != null && !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("Formato de arquivo '%s' não permitido. Formatos permitidos: PDF, JPG, PNG, DOCX, WEBP",
                                contentType))
                        .addConstraintViolation();
                return false;
            }

            if (originalFilename != null) {
                String extension = getFileExtension(originalFilename).toLowerCase();
                if (!ALLOWED_EXTENSIONS.contains(extension)) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            String.format("Extensão de arquivo '%s' não permitida. Formatos permitidos: PDF, JPG, PNG, DOCX, WEBP",
                                    extension))
                            .addConstraintViolation();
                    return false;
                }
            }

            return true;
        }

        private static String getFileExtension(String filename) {
            int lastDotIndex = filename.lastIndexOf('.');
            if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
                return "";
            }
            return filename.substring(lastDotIndex + 1);
        }
    }

    class ListValidator implements ConstraintValidator<ValidFileFormat, List<MultipartFile>> {
        @Override
        public boolean isValid(List<MultipartFile> files, ConstraintValidatorContext context) {
            if (files == null || files.isEmpty()) {
                return true;
            }
            for (MultipartFile file : files) {
                if (!ValidationHelper.isValidFile(file, context)) {
                    return false;
                }
            }
            return true;
        }
    }

    class SingleFileValidator implements ConstraintValidator<ValidFileFormat, MultipartFile> {
        @Override
        public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
            return ValidationHelper.isValidFile(file, context);
        }
    }
}

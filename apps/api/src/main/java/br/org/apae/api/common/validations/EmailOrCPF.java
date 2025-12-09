
package br.org.apae.api.common.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Pattern.Flag;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailOrCPF.Validator.class)
@Documented
public @interface EmailOrCPF {
  String message() default "Informe email ou cpf válido";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String emailRegexp() default ".*";

  Pattern.Flag[] emailFlags() default {};

  boolean checkCpfDigits() default true;

  class Validator implements ConstraintValidator<EmailOrCPF, String> {
    private final EmailValidator emailValidator = new EmailValidator();
    private final CPF.Validator cpfValidator = new CPF.Validator();

    @Override
    public void initialize(EmailOrCPF constraintAnnotation) {
      CPF cpfAnnotation = new CPF() {
        @Override
        public Class<? extends java.lang.annotation.Annotation> annotationType() {
          return constraintAnnotation.annotationType();
        }

        @Override
        public String message() {
          return constraintAnnotation.message();
        }

        @Override
        public Class<?>[] groups() {
          return constraintAnnotation.groups();
        }

        @Override
        public Class<? extends Payload>[] payload() {
          return constraintAnnotation.payload();
        }

        @Override
        public boolean checkDigits() {
          return constraintAnnotation.checkCpfDigits();
        }
      };

      Email emailAnnotation = new Email() {
        @Override
        public Class<? extends java.lang.annotation.Annotation> annotationType() {
          return constraintAnnotation.annotationType();
        }

        @Override
        public String message() {
          return constraintAnnotation.message();
        }

        @Override
        public Class<?>[] groups() {
          return constraintAnnotation.groups();
        }

        @Override
        public Class<? extends Payload>[] payload() {
          return constraintAnnotation.payload();
        }

        @Override
        public Flag[] flags() {
          return constraintAnnotation.emailFlags();
        }

        @Override
        public String regexp() {
          return constraintAnnotation.emailRegexp();
        }
      };

      cpfValidator.initialize(cpfAnnotation);
      emailValidator.initialize(emailAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null || value.isEmpty()) {
        return true;
      }

      if (cpfValidator.isValid(value, context)) {
        return true;
      }

      if (emailValidator.isValid(value, context)) {
        return true;
      }

      return false;
    }
  }
}

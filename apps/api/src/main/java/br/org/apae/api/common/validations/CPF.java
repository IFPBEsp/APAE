package br.org.apae.api.common.validations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CPF.Validator.class)
@Documented
public @interface CPF {
  String message() default "CPF inválido";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean checkDigits() default true;

  class Validator implements ConstraintValidator<CPF, String> {
    private static final Pattern CPF_PATTERN = Pattern.compile(
        "^\\d{3}\\.?\\d{3}\\.?\\d{3}-?\\d{2}$");
    private static final Integer FIRST_DIGIT_INDEX = 9;
    private static final Integer SECOND_DIGIT_INDEX = 10;

    private boolean validateDigits;

    private static int calculateFirstDigit(String cpf) {
      int sum = IntStream.range(0, FIRST_DIGIT_INDEX)
          .map((int index) -> Character.getNumericValue(cpf.charAt(index)) * (10 - index))
          .sum();

      int firstDigit = 11 - (sum % 11);
      return firstDigit < 10 ? firstDigit : 0;
    }

    private static int calculateSecondDigit(String cpf) {
      int sum = IntStream.range(0, SECOND_DIGIT_INDEX)
          .map((int index) -> Character.getNumericValue(cpf.charAt(index)) * (11 - index))
          .sum();

      int secondDigit = 11 - (sum % 11);
      return secondDigit < 10 ? secondDigit : 0;
    }

    private static boolean validateFirstDigit(String cpf) {
      return Character.getNumericValue(
          cpf.charAt(FIRST_DIGIT_INDEX)) == calculateFirstDigit(cpf);
    }

    private static boolean validateSecondDigit(String cpf) {
      return Character.getNumericValue(
          cpf.charAt(SECOND_DIGIT_INDEX)) == calculateSecondDigit(cpf);
    }

    private boolean validateVerificationDigits(String cpf) {
      if (!validateDigits) {
        return true;
      }

      return validateFirstDigit(cpf) && validateSecondDigit(cpf);
    }

    @Override
    public void initialize(CPF constraintAnnotation) {
      this.validateDigits = constraintAnnotation.checkDigits();
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
      if (cpf == null || cpf.isEmpty()) {
        return true;
      }

      return CPF_PATTERN.matcher(cpf).matches() && validateVerificationDigits(
          cpf.replaceAll("\\D", ""));
    }
  }
}

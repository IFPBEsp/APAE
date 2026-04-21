import { UseFormSetError, FieldValues, Path } from "react-hook-form";

/**
 * Interface que espelha o ValidationErrorResponse do Java.
 */
interface ApiFieldError {
  field: string;
  message: string;
}

interface ApiErrorResponse {
  fields?: ApiFieldError[];
  message?: string;
}

/**
 * Mapeia erros de validação do backend para os campos do formulário no frontend.
 * Resolve nomes de campos aninhados e mapeia campos específicos de API para a UI.
 * * @param errorData Objeto de erro retornado pela API contendo a lista de campos.
 * @param setError Função setError proveniente do useForm do React Hook Form.
 */
export function handleBackendValidationErrors<T extends FieldValues>(
  errorData: ApiErrorResponse,
  setError: UseFormSetError<T>,
) {
  if (errorData.fields && Array.isArray(errorData.fields)) {
    errorData.fields.forEach((err) => {
      let fieldName = err.field;

      if (fieldName.includes(".")) {
        const parts = fieldName.split(".");
        fieldName = parts[parts.length - 1];
      }

      if (fieldName === "number") fieldName = "number";
      if (fieldName === "neighborhood") fieldName = "neighborhood";
      if (fieldName === "fullName") fieldName = "name";
      if (fieldName === "contact") fieldName = "phone";
      if (fieldName === "familyIncome") fieldName = "householdIncome";

      setError(fieldName as Path<T>, {
        type: "server",
        message: err.message,
      });
    });
  }
}

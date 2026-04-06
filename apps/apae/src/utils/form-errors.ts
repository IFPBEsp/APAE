import { UseFormSetError } from "react-hook-form";

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
export function handleBackendValidationErrors<TFieldValues extends Record<string, unknown>>(
  errorData: ApiErrorResponse,
  setError: UseFormSetError<TFieldValues>,
) {
  if (errorData.fields && Array.isArray(errorData.fields)) {
    errorData.fields.forEach((err) => {
      let fieldName = err.field;

      if (fieldName.includes(".")) {
        const parts = fieldName.split(".");
        fieldName = parts[parts.length - 1];
      }

      if (fieldName === "number") fieldName = "street";
      if (fieldName === "neighborhood") fieldName = "district";
      if (fieldName === "fullName") fieldName = "name";
      if (fieldName === "contact") fieldName = "phone";
      if (fieldName === "familyIncome") fieldName = "householdIncome";

      setError(fieldName as Parameters<typeof setError>[0], {
        type: "server",
        message: err.message,
      });
    });
  }
}

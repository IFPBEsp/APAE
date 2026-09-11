import { FieldValues, Path, UseFormSetError } from "react-hook-form";

/*
 * Interface that mirrors Java's ValidationErrorResponse.
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
 * Maps backend validation errors to frontend form fields.
 * Resolves nested field names and maps API specific fields to the UI.
 * * @param errorData Error object returned by the API containing the field list.
 * @param setError setError function from React Hook Form's useForm.
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

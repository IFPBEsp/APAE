import { FieldValues, Path, UseFormSetError } from "react-hook-form";

interface ApiFieldError {
  field: string;
  message: string;
}

interface ApiErrorResponse {
  fields?: ApiFieldError[];
  message?: string;
}

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

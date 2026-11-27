"use client";

import { useEffect, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  ALLOWED_DOCUMENT_ACCEPT,
  DOCUMENT_FORMAT_HINT,
  validateDocumentFile,
} from "@/schemas/shared/document.schema";

export interface DocumentUploadProps {
  id?: string;
  label?: string;
  file?: File | null;
  onFileChange: (file: File | null) => void;
  onErrorChange?: (error: string | null) => void;
  externalError?: string | null;
  disabled?: boolean;
  showSelectedFile?: boolean;
  showRemoveButton?: boolean;
  clearInputOnSelect?: boolean;
}

export function DocumentUpload({
  id,
  label,
  file = null,
  onFileChange,
  onErrorChange,
  externalError = null,
  disabled = false,
  showSelectedFile = true,
  showRemoveButton = true,
  clearInputOnSelect = false,
}: DocumentUploadProps) {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const [internalError, setInternalError] = useState<string | null>(null);

  useEffect(() => {
    if (!file && inputRef.current) {
      inputRef.current.value = "";
    }
  }, [file]);

  const updateError = (err: string | null) => {
    setInternalError(err);
    onErrorChange?.(err);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const selected = event.target.files?.[0] ?? null;

    if (!selected) {
      updateError(null);
      onFileChange(null);
      return;
    }

    const validationError = validateDocumentFile(selected);
    if (validationError) {
      if (inputRef.current) {
        inputRef.current.value = "";
      }
      onFileChange(null);
      updateError(validationError);
      return;
    }

    updateError(null);
    onFileChange(selected);
    if (clearInputOnSelect && inputRef.current) {
      inputRef.current.value = "";
    }
  };

  const handleRemove = () => {
    if (inputRef.current) {
      inputRef.current.value = "";
    }
    updateError(null);
    onFileChange(null);
  };

  const errorMessage = internalError ?? externalError;

  return (
    <div className="space-y-2 w-full">
      {label && (
        <label
          htmlFor={id}
          className="text-sm font-medium text-gray-700 block"
        >
          {label}
        </label>
      )}
      <Input
        id={id}
        ref={inputRef}
        type="file"
        accept={ALLOWED_DOCUMENT_ACCEPT}
        disabled={disabled}
        className="cursor-pointer bg-white"
        onChange={handleChange}
      />
      <p className="text-xs text-gray-500">{DOCUMENT_FORMAT_HINT}</p>
      {showSelectedFile && file && (
        <div className="flex items-center justify-between gap-2 text-sm bg-gray-50 p-2 rounded border">
          <span
            className="text-xs text-gray-600 truncate"
            title={file.name}
          >
            Selecionado: {file.name}
          </span>
          {showRemoveButton && (
            <Button
              type="button"
              variant="outline"
              size="sm"
              disabled={disabled}
              onClick={handleRemove}
            >
              Remover
            </Button>
          )}
        </div>
      )}
      {errorMessage && (
        <p className="text-sm text-red-500">{errorMessage}</p>
      )}
    </div>
  );
}

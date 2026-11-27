"use client";

import { useEffect, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  ALLOWED_DOCUMENT_ACCEPT,
  DOCUMENT_FORMAT_HINT,
  validateDocumentFile,
} from "@/schemas/shared/document.schema";

export interface RejectedDocumentFile {
  name: string;
  reason: string;
}

export interface DocumentUploadProps {
  id?: string;
  label?: string;
  file?: File | null;
  onFileChange?: (file: File | null) => void;
  files?: File[];
  onFilesChange?: (files: File[]) => void;
  multiple?: boolean;
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
  files = [],
  onFilesChange,
  multiple = false,
  onErrorChange,
  externalError = null,
  disabled = false,
  showSelectedFile = true,
  showRemoveButton = true,
  clearInputOnSelect = false,
}: DocumentUploadProps) {
  const inputRef = useRef<HTMLInputElement | null>(null);
  const [internalError, setInternalError] = useState<string | null>(null);
  const [rejectedFiles, setRejectedFiles] = useState<RejectedDocumentFile[]>([]);

  useEffect(() => {
    if (!multiple && !file && inputRef.current) {
      inputRef.current.value = "";
    }
  }, [file, multiple]);

  useEffect(() => {
    if (multiple && files.length === 0 && inputRef.current) {
      inputRef.current.value = "";
    }
  }, [files.length, multiple]);

  const updateError = (err: string | null) => {
    setInternalError(err);
    onErrorChange?.(err);
  };

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (multiple) {
      const selectedFiles = Array.from(event.target.files ?? []);
      if (selectedFiles.length === 0) {
        setRejectedFiles([]);
        updateError(null);
        onFilesChange?.([]);
        return;
      }

      const valid: File[] = [];
      const rejected: RejectedDocumentFile[] = [];

      for (const item of selectedFiles) {
        const validationError = validateDocumentFile(item);
        if (validationError) {
          rejected.push({ name: item.name, reason: validationError });
        } else {
          valid.push(item);
        }
      }

      setRejectedFiles(rejected);
      onFilesChange?.(valid);

      if (valid.length === 0 && inputRef.current) {
        inputRef.current.value = "";
      }
      return;
    }

    const selected = event.target.files?.[0] ?? null;

    if (!selected) {
      updateError(null);
      onFileChange?.(null);
      return;
    }

    const validationError = validateDocumentFile(selected);
    if (validationError) {
      if (inputRef.current) {
        inputRef.current.value = "";
      }
      onFileChange?.(null);
      updateError(validationError);
      return;
    }

    updateError(null);
    onFileChange?.(selected);
    if (clearInputOnSelect && inputRef.current) {
      inputRef.current.value = "";
    }
  };

  const handleRemove = () => {
    if (inputRef.current) {
      inputRef.current.value = "";
    }
    setRejectedFiles([]);
    updateError(null);
    if (multiple) {
      onFilesChange?.([]);
    } else {
      onFileChange?.(null);
    }
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
        multiple={multiple}
        accept={ALLOWED_DOCUMENT_ACCEPT}
        disabled={disabled}
        className="cursor-pointer bg-white"
        onChange={handleChange}
      />
      <p className="text-xs text-gray-500">{DOCUMENT_FORMAT_HINT}</p>
      {showSelectedFile && !multiple && file && (
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
      {showSelectedFile && multiple && files.length > 0 && (
        <div className="flex items-center justify-between gap-2 text-sm bg-gray-50 p-2 rounded border">
          <span
            className="text-xs text-gray-600 truncate"
            title={files.map((f) => f.name).join(", ")}
          >
            Selecionado(s): {files.map((f) => f.name).join(", ")}
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
      {rejectedFiles.length > 0 && (
        <ul className="space-y-1 text-sm text-red-500">
          {rejectedFiles.map((item, index) => (
            <li key={`${item.name}-${index}`}>
              {item.name}: {item.reason}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}

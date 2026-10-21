import { useEffect, useRef, useState, useCallback } from "react";
import { photoFileSchema } from "@/schemas/shared/photo.schema";
import { bffFetch } from "@/lib/bff-client";

export function validatePhotoFile(file: File): string | null {
  const result = photoFileSchema.safeParse(file);
  return result.success ? null : (result.error.issues[0]?.message ?? "Arquivo inválido");
}

export function usePhotoUpload(options?: { buildUrl: (id: string) => string; method: "PUT" | "PATCH"; fieldName?: string }) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [selectedPhoto, setSelectedPhoto] = useState<File | null>(null);
  const [photoPreviewUrl, setPhotoPreviewUrl] = useState<string | null>(null);
  const [photoError, setPhotoError] = useState<string | null>(null);
  const [photoSuccess, setPhotoSuccess] = useState(false);

  const selectedPhotoRef = useRef(selectedPhoto);
  selectedPhotoRef.current = selectedPhoto;

  const optionsRef = useRef(options);
  optionsRef.current = options;

  useEffect(() => {
    if (!selectedPhoto) { setPhotoPreviewUrl(null); return; }
    const url = URL.createObjectURL(selectedPhoto);
    setPhotoPreviewUrl(url);
    return () => URL.revokeObjectURL(url);
  }, [selectedPhoto]);

  const clearPhoto = useCallback(() => {
    setSelectedPhoto(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
  }, []);

  const selectPhoto = useCallback((file: File | null): boolean => {
    setPhotoError(null);
    setPhotoSuccess(false);
    if (!file) { clearPhoto(); return false; }
    const error = validatePhotoFile(file);
    if (error) { setPhotoError(error); clearPhoto(); return false; }
    setSelectedPhoto(file);
    return true;
  }, [clearPhoto]);

  const uploadPhoto = useCallback(async (id: string): Promise<boolean> => {
    if (!selectedPhotoRef.current || !optionsRef.current) return true;
    setPhotoError(null);
    setPhotoSuccess(false);
    try {
      const photoData = new FormData();
      photoData.append(optionsRef.current.fieldName ?? "file", selectedPhotoRef.current);
      const response = await bffFetch(optionsRef.current.buildUrl(id), {
        method: optionsRef.current.method,
        body: photoData,
      });
      if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        let errMsg = "Erro ao enviar foto";
        if (typeof body === "string") {
            errMsg = body;
        } else if (typeof body?.message === "string") {
            errMsg = body.message;
        } else if (typeof body?.message?.message === "string") {
            errMsg = body.message.message;
        }
        throw new Error(errMsg);
      }
      setPhotoSuccess(true);
      setSelectedPhoto(null);
      return true;
    } catch (e: unknown) {
      setPhotoError((e as Error)?.message ?? "Erro ao enviar foto");
      return false;
    }
  }, []);

  return {
    fileInputRef,
    selectedPhoto,
    setSelectedPhoto,
    selectPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
    clearPhoto,
    uploadPhoto,
  };
}

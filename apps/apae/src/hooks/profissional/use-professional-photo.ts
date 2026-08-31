import { useEffect, useRef, useState } from "react";

export function useProfessionalPhoto() {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [selectedPhoto, setSelectedPhoto] = useState<File | null>(null);
  const [photoPreviewUrl, setPhotoPreviewUrl] = useState<string | null>(null);
  const [photoError, setPhotoError] = useState<string | null>(null);
  const [photoSuccess, setPhotoSuccess] = useState(false);

  useEffect(() => {
    if (!selectedPhoto) {
      setPhotoPreviewUrl(null);
      return;
    }
    const url = URL.createObjectURL(selectedPhoto);
    setPhotoPreviewUrl(url);
    return () => URL.revokeObjectURL(url);
  }, [selectedPhoto]);

  function clearPhoto() {
    setSelectedPhoto(null);
    if (fileInputRef.current) fileInputRef.current.value = "";
  }

  async function uploadPhoto(professionalId: string): Promise<boolean> {
    if (!selectedPhoto) return true;
    setPhotoError(null);
    setPhotoSuccess(false);
    try {
      const photoData = new FormData();
      photoData.append("file", selectedPhoto);
      const response = await fetch(`/apae-geral/api/professionals/${professionalId}/photo`, {
        method: "PATCH",
        body: photoData,
      });
      if (!response.ok) {
        const body = await response.json().catch(() => ({}));
        throw new Error(body?.message ?? "Erro ao enviar foto");
      }
      setPhotoSuccess(true);
      setSelectedPhoto(null);
      return true;
    } catch (e: unknown) {
      setPhotoError((e as Error)?.message ?? "Erro ao enviar foto");
      return false;
    }
  }

  return {
    fileInputRef,
    selectedPhoto,
    setSelectedPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
    clearPhoto,
    uploadPhoto,
  };
}

import { useEffect, useRef, useState } from "react";

export function useProfessionalRegisterPhoto(photoFile: File | null| undefined) {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);

  useEffect(() => {
    if (photoFile instanceof File) {
      const url = URL.createObjectURL(photoFile);
      setPreviewUrl(url);
      return () => URL.revokeObjectURL(url);
    } else {
      setPreviewUrl(null);
    }
  }, [photoFile]);

  return { fileInputRef, previewUrl };
}
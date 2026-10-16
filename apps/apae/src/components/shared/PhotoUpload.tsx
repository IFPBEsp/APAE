"use client";

import { RefObject } from "react";
import { User } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { useFormContext } from "react-hook-form";
import { MAX_FILE_SIZE_LABEL } from "@/lib/constants";
import { ALLOWED_IMAGE_TYPES } from "@/schemas/shared/photo.schema";
import { validatePhotoFile } from "@/hooks/use-photo-upload";

export interface PhotoUploadProps {
  label?: string;
  name?: string;
  fileInputRef: RefObject<HTMLInputElement | null>;
  selectedPhoto: File | null;
  photoPreviewUrl: string | null;
  profilePhotoUrl?: string | null;
  photoError: string | null;
  photoSuccess: boolean;
  setSelectedPhoto: (file: File | null) => void;
  clearPhoto: () => void;
}

export function PhotoUpload({
  label,
  name = "photo",
  fileInputRef,
  selectedPhoto,
  photoPreviewUrl,
  profilePhotoUrl,
  photoError,
  photoSuccess,
  setSelectedPhoto,
  clearPhoto,
}: PhotoUploadProps) {
  const { control, setValue } = useFormContext();

  return (
    <FormField
      control={control}
      name={name}
      render={() => (
        <FormItem>
          <FormLabel className="text-sm font-medium">{label ?? "Selecione uma foto*"}</FormLabel>
          <FormControl>
            <div className="flex flex-col items-start gap-4 w-full">
              <input
                ref={fileInputRef}
                type="file"
                className="hidden"
                accept={ALLOWED_IMAGE_TYPES.join(",")}
                onChange={(e) => {
                  const file = e.target.files?.[0];
                  if (!file) { setValue(name, undefined); clearPhoto(); return; }
                  const error = validatePhotoFile(file);
                  if (error) {
                    alert(error);
                    clearPhoto();
                    setValue(name, undefined);
                    return;
                  }
                  setValue(name, file);
                  setSelectedPhoto(file);
                }}
              />
              <button
                type="button"
                onClick={() => fileInputRef.current?.click()}
                className="relative group mr-auto rounded-full transition-transform hover:scale-105"
              >
                <Avatar className="w-32 h-32 border-2 border-dashed border-gray-300 bg-gray-50">
                  <AvatarImage src={photoPreviewUrl ?? profilePhotoUrl ?? undefined} alt="Foto do perfil" />
                  <AvatarFallback className="bg-transparent">
                    <User className="w-12 h-12 text-gray-400" />
                  </AvatarFallback>
                </Avatar>
                <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity bg-black/20 rounded-full">
                  <span className="bg-white text-black text-[10px] font-bold px-2 py-1 rounded shadow-sm">Escolher foto</span>
                </div>
              </button>
              <p className="text-xs text-gray-500">PNG, JPG ou WEBP até {MAX_FILE_SIZE_LABEL}</p>
              {selectedPhoto && (
                <div className="flex items-center gap-2">
                  <p className="text-xs text-gray-600">Selecionado: {selectedPhoto.name}</p>
                  <Button type="button" variant="outline" size="sm" onClick={() => { clearPhoto(); setValue(name, undefined); }}>
                    Remover
                  </Button>
                </div>
              )}
            </div>
          </FormControl>
          {photoError && <p className="text-sm text-red-500">{photoError}</p>}
          {photoSuccess && <p className="text-sm text-green-600">Foto enviada com sucesso!</p>}
          <FormMessage />
        </FormItem>
      )}
    />
  );
}

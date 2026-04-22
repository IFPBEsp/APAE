"use client";

import Image from "next/image";
import { useState } from "react";

import * as React from "react";
import { FileText, ExternalLink } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "./ui/button";

interface Props {
  file: {
    fileName: string;
    link: string;
    type?: string;
    originalName?: string;
  };
}

export default function FileCard({ file }: Props) {
  const [open, setOpen] = React.useState(false);

  // Estado para o Modal (Preview grande)
  const [modalViewType, setModalViewType] = React.useState<'img' | 'iframe'>('img');

  // NOVO: Estado para a Miniatura (Card pequeno)
  const [thumbnailView, setThumbnailView] = React.useState<'img' | 'icon'>('img');

  React.useEffect(() => {
    if (open) {
      setModalViewType('img');
    }
  }, [open]);

  return (
    <>
      <div
        className="cursor-pointer p-3 border rounded hover:bg-gray-100 overflow-hidden flex flex-col"
        onClick={() => setOpen(true)}
      >
        <p className="truncate mb-2 text-sm font-medium">{file.fileName}</p>

        {/* ÁREA DA MINIATURA */}
        <div className="flex flex-col items-center justify-center bg-blue-50 text-blue-900 rounded-md w-full h-32 border border-blue-100 overflow-hidden relative">

          {thumbnailView === 'img' ? (
             <img
               src={file.link}
               alt={file.fileName}
               // object-cover faz a imagem preencher o quadradinho todo de forma elegante
               className="w-full h-full object-cover"
               // Se falhar (for PDF), troca para o ícone
               onError={() => setThumbnailView('icon')}
             />
          ) : (
            <>
              <FileText size={40} strokeWidth={1.5} className="mb-1 text-blue-800" />
              <span className="text-[10px] font-bold uppercase tracking-wider text-blue-800">
                Documento
              </span>
            </>
          )}

        </div>
      </div>

      {/* Preview Modal (MANTIDO IGUAL AO QUE FUNCIONOU) */}
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-[900px] max-h-[90vh] overflow-hidden bg-white">
          <DialogHeader className="flex flex-row items-center justify-between">
            <DialogTitle className="truncate pr-4">{file.fileName}</DialogTitle>
          </DialogHeader>

          {/* Área de Preview grande */}
          <div className="flex justify-center items-center w-full h-[60vh] bg-gray-50 rounded-lg overflow-hidden relative">
            {modalViewType === 'img' && (
              <img
                src={file.link}
                alt={file.fileName}
                className="w-full h-full object-contain"
                onError={() => setModalViewType('iframe')}
              />
            )}

            {modalViewType === 'iframe' && (
              <iframe
                src={file.link}
                title={file.fileName}
                className="w-full h-full border-0"
              />
            )}
          </div>

          {/* Botão para abrir em nova guia */}
          <div className="flex justify-center mt-4 pb-2">
            <Button
              variant="default"
              onClick={() => window.open(file.link, "_blank")}
              className="gap-2"
            >
              <ExternalLink size={18} />
              Abrir em nova guia
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}

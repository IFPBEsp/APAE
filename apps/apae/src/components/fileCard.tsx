"use client";

import * as React from "react";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "./ui/button";
import { FileText } from "lucide-react";
import { useState } from "react";


interface Props {
  file: {
    fileName: string;
    link: string;
  };
}

interface FileCardProps {
  file: {
    fileName: string;
    link: string;
  };
}

export default function FileCard({ file }: Props) {
  const [open, setOpen] = React.useState(false);

   // Se NÃO for imagem, o sistema vai assumir que é um PDF/Documento.
  const isImage = file.link?.toLowerCase().match(/\.(jpeg|jpg|gif|png|webp)$/) != null;
  
  const [isPdf, setIsPdf] = useState(false);


  return (
   <>
      <div
        className="cursor-pointer p-3 border rounded hover:bg-gray-100 overflow-hidden"
        onClick={() => setOpen(true)}
      >
        <p className="truncate font-medium">{file.fileName}</p>
        
        {/* Se o link tem '.pdf' OU se marcarmos como PDF por erro da imagem, mostra o ícone */}
        {file.link?.toLowerCase().includes('.pdf') || isPdf ? (
          <div className="w-full h-32 mt-2 flex flex-col items-center justify-center text-red-500 bg-gray-50 rounded">
            <FileText size={48} strokeWidth={1.5} />
            <span className="text-xs font-bold mt-2">PDF</span>
          </div>
        ) : (
          <img
            src={file.link}
            alt={file.fileName}
            className="w-full h-32 object-contain mt-2"
            onError={() => setIsPdf(true)} // <-- A MÁGICA: Se quebrar a imagem, ele avisa que é PDF!
          />
        )}
      </div>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-[800px] h-[85vh] flex flex-col">
          <DialogHeader>
            <DialogTitle>{file.fileName}</DialogTitle>
          </DialogHeader>
          <div className="flex-1 overflow-hidden flex justify-center items-center">
            
            {/* Mesma regra na hora de abrir gigante */}
            {file.link?.toLowerCase().includes('.pdf') || isPdf ? (
              <iframe
                src={file.link}
                className="w-full h-full border-none rounded"
              />
            ) : (
              <img
                src={file.link}
                alt={file.fileName}
                className="max-w-full max-h-full object-contain"
              />
            )}

          </div>
          <DialogFooter>
            <Button
              variant="secondary"
              onClick={() => window.open(file.link, '_blank')}
              className="cursor-pointer hover:bg-gray-300 hover:text-black transition-colors"
            >
              Visualizar Arquivo
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}

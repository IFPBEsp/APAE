"use client";

import * as React from "react";
import { FileText } from "lucide-react";

interface Props {
  file: {
    fileName: string;
    link: string;
  };
}

export default function FileCard({ file }: Props) {
  // Detecta se é imagem por extensão
  const isImage = file.link?.toLowerCase().match(/\.(jpeg|jpg|gif|png|webp)$/) != null;
  const isPdf = file.link?.toLowerCase().includes('.pdf');

  return (
    <div className="cursor-pointer p-3 border rounded hover:bg-gray-100 overflow-hidden bg-white">
      <p className="truncate font-medium text-sm">{file.fileName}</p>

      {/* Preview no card: PDF ou Imagem */}
      {isPdf ? (
        <div className="w-full h-32 mt-2 flex flex-col items-center justify-center text-red-500 bg-gray-50 rounded">
          <FileText size={48} strokeWidth={1.5} />
          <span className="text-xs font-bold mt-2">PDF</span>
        </div>
      ) : isImage ? (
        <img
          src={file.link}
          alt={file.fileName}
          className="w-full h-32 object-cover mt-2 rounded"
        />
      ) : (
        <div className="w-full h-32 mt-2 flex flex-col items-center justify-center text-gray-400 bg-gray-50 rounded">
          <FileText size={48} strokeWidth={1.5} />
          <span className="text-xs font-medium mt-2">Documento</span>
        </div>
      )}
    </div>
  );
}

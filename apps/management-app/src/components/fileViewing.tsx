"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";

import FileFilter from "./fileViewing/fileFilter";
import FileCard from "./fileViewing/fileCard";
import { mockFiles, type FileItem } from "@/lib/mockFiles";

export default function FileViewer() {
  const router = useRouter();
  const [yearFilter, setYearFilter] = React.useState<string | null>(null);
  const [typeFilter, setTypeFilter] = React.useState<string | null>(null);

  const filteredFiles = React.useMemo(() => {
    return mockFiles.filter((file) => {
      const matchesYear = !yearFilter || file.year === yearFilter;
      const matchesType = !typeFilter || file.type === typeFilter;
      return matchesYear && matchesType;
    });
  }, [yearFilter, typeFilter]);

  const brandColor = "text-[#0d4f97]";

  return (
    <main className="pt-6 md:pt-12 px-4 py-6 max-w-7xl mx-auto font-baloo">
      
      {/* Botão voltar(mobile) */}
      <button
        onClick={() => router.back()}
        className={`absolute top-4 left-4 ${brandColor} hover:underline flex items-center gap-1 block lg:hidden`}
      >
        <ArrowLeft size={20} />
      </button>

      {/* Layout mobile */}
      <div className={`flex flex-col mt-6 md:hidden ${brandColor}`}>
        <h1 className="text-xl font-bold mb-6">Documentos</h1>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter}
        />
      </div>

      {/* Layout desktop */}
      <div className="hidden md:flex items-center justify-center bg-white rounded-4xl shadow-md py-3 px-8 max-w-4xl mx-auto mb-10 gap-12 border text-[#0d4f97]">
        <h1 className="text-xl font-bold whitespace-nowrap">Documentos</h1>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter}
        />
      </div>


      <div className="grid grid-cols-2 lg:grid-cols-6 gap-4 mt-4">
        {filteredFiles.length === 0 ? (
          <p>Nenhum arquivo encontrado.</p>
        ) : (
          filteredFiles.map((file: FileItem) => (
            <FileCard key={file.id} name={file.name} url={file.url} />
          ))
        )}
      </div>
    </main>
  );
}

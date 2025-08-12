"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";

import FileFilter from "./fileViewing/fileFilter";
import FileCard from "./fileViewing/fileCard";
import { mockFiles, type FileItem } from "@/lib/mockFiles";

interface FileViewerProps {
  initialCategory: "pessoal" | "medico" | "escolar";
}

const documentCategory = {
  pessoal: "Documentos pessoais",
  medico: "Documentos médicos",
  escolar: "Documentos escolares",
};

const typeDocument ={
  pessoal:['rg','cpf', 'certidão de nascimento', 'comprovante de residência'],
  medico:['laudo','encaminhamento'],
  escolar:['historico','declaração de matrícula'],
}

export default function FileViewer({ initialCategory }: FileViewerProps) {
  const router = useRouter();
  const [category, setCategory] = React.useState<"pessoal" | "medico" | "escolar">(initialCategory);
  const [yearFilter, setYearFilter] = React.useState<string | null>(null);
  const [typeFilter, setTypeFilter] = React.useState<string | null>(null);

  const categoryTypes = typeDocument[category];

  const filteredFiles = React.useMemo(() => {
    return mockFiles.filter((file) => {
      const matchesCategory = file.category === category;
      const matchesYear = !yearFilter || file.year === yearFilter;
      const matchesType = !typeFilter || file.type.toLowerCase() === typeFilter.toLowerCase();
      return matchesCategory && matchesYear && matchesType;
    });
  }, [category, yearFilter, typeFilter]);

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
        <h1 className="text-xl font-bold mb-6">{documentCategory[category]}</h1>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter} 
          categoryTypes={categoryTypes}        />
      </div>

      {/* Layout desktop */}
      <div className="hidden md:flex items-center justify-center bg-white rounded-4xl shadow-md py-3 px-8 max-w-4xl mx-auto mb-10 gap-12 border text-[#0d4f97]">
        <h1 className="text-xl font-bold whitespace-nowrap">{documentCategory[category]}</h1>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter} 
          categoryTypes={categoryTypes}        />
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

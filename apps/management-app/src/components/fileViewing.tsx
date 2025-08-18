"use client";

import * as React from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft } from "lucide-react";

import FileFilter from "./fileViewing/fileFilter";
import FileCard from "./fileViewing/fileCard";
import { listFilteredDocuments } from "@/service/fileViewingService";

export interface FileItem {
  id: string;
  name: string;
  category: "pessoal" | "medico" | "escolar";
  type: string;
  url: string;
  year: string;
}

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
  const [files, setFiles] = React.useState<FileItem[]>([]);

  const categoryTypes = typeDocument[category];

React.useEffect(() => {
    async function fetchDocuments() {
      try {
        if (!typeFilter || !yearFilter) return;

        const response = await listFilteredDocuments(
          "",
          category,
          parseInt(yearFilter),
          typeFilter,
          ""
        );

        const converted = response.documents.map((doc, index) => ({
          id: index.toString(),
          name: doc.fileName,
          url: doc.url,
          year: new Date(doc.createdAt).getFullYear().toString(),
          category,
          type: typeFilter,
        }));

        setFiles(converted);
      } catch (err) {
        console.error("Erro ao buscar documentos:", err);
      }
    }

    fetchDocuments();
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
        {files.length === 0 ? (
          <p>Nenhum arquivo encontrado.</p>
        ) : (
          files.map((file: FileItem) => (
            
            <FileCard key={file.id} file={{fileName: file.name, link: file.url}} />
          ))
        )}
      </div>
    </main>
  );
}

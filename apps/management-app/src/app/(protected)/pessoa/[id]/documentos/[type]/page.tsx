"use client";

import * as React from "react";
import { useRouter, useParams } from "next/navigation";
import { ArrowLeft } from "lucide-react";

import FileCard from "@/components/fileCard";
import FileFilter from "@/components/fileFilter";
import { toast } from "react-toastify";

export interface FileItem {
  id: string;
  name: string;
  category: "pessoal" | "medico" | "escolar";
  type: string;
  url: string;
  year: string;
}

const documentCategory = {
  pessoal: "Documentos pessoais",
  medico: "Documentos médicos",
  escolar: "Documentos escolares",
};

const typeDocument = {
  pessoal: ["rg", "cpf", "certidão de nascimento", "comprovante de residência"],
  medico: ["laudo", "encaminhamento"],
  escolar: ["historico", "declaração de matrícula"],
};

export default function FileViewer() {
  const router = useRouter();
  const params = useParams();
  const patientId = params?.id as string;
  const category = params?.type as "pessoal" | "medico" | "escolar";
  const [yearFilter, setYearFilter] = React.useState<string>(
    new Date().getFullYear().toString()
  );
  const [typeFilter, setTypeFilter] = React.useState<string>("LAUDO");
  const [files, setFiles] = React.useState<FileItem[]>([]);

  const categoryTypes = typeDocument[category];

  React.useEffect(() => {
    async function fetchDocuments() {
      try {
        if (!patientId || !typeFilter || !yearFilter) return;
        const params = new URLSearchParams({
          category,
          year: yearFilter,
          ...(typeFilter && { type: typeFilter }),
        });

        const response = await fetch(
          `/api/documents/${patientId}?${params.toString()}`
        );
        if (!response.ok) {
          throw new Error("Erro ao buscar os documentos");
        }

        const data = await response.json();

        const converted = data.urls.map((doc: any, index: number) => ({
          id: index.toString(),
          name: doc.fileName,
          url: doc.link,
          year: yearFilter,
          category,
          type: typeFilter,
        }));

        setFiles(converted);
      } catch (err) {
        console.error("Erro ao buscar documentos:", err);
        toast.error("Erro ao buscar documentos");
        setFiles([]);
      }
    }

    fetchDocuments();
  }, [patientId, category, yearFilter, typeFilter]);

  const brandColor = "text-[#0d4f97]";

  return (
    <main className="pt-6 md:pt-12 px-4 py-6 max-w-7xl mx-auto font-baloo">
      <div
        className={`flex flex-col mt-6 md:hidden ${brandColor} items-center`}
      >
        <div className="flex items-center justify-start bg-white rounded-4xl shadow-md mb-6 gap-6 border w-full p-4">
          <button
            onClick={() => router.back()}
            className="flex items-center justify-center"
          >
            <ArrowLeft size={20} />
          </button>
          <h1 className="text-xl font-bold">{documentCategory[category]}</h1>
        </div>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter}
          categoryTypes={categoryTypes}
        />
      </div>
      <div className="hidden md:flex items-center justify-center bg-white rounded-4xl shadow-md py-3 px-8 max-w-4xl mx-auto mb-10 gap-12 border text-[#0d4f97]">
        <button onClick={() => router.back()}>
          <ArrowLeft size={20} />
        </button>
        <h1 className="text-xl font-bold whitespace-nowrap">
          {documentCategory[category]}
        </h1>
        <FileFilter
          year={yearFilter}
          type={typeFilter}
          onYearChange={setYearFilter}
          onTypeChange={setTypeFilter}
          categoryTypes={categoryTypes}
        />
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-6 gap-4 mt-4">
        {files.length === 0 ? (
          <p>Nenhum arquivo encontrado.</p>
        ) : (
          files.map((file: FileItem) => (
            <FileCard
              key={file.id}
              file={{ fileName: file.name, link: file.url }}
            />
          ))
        )}
      </div>
    </main>
  );
}

"use client";

import * as React from "react";
import { useRouter, useParams } from "next/navigation";
import { ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import FileCard from "@/components/fileCard";
import { toast } from "react-toastify";

export interface FileItem {
  id: string;
  name: string;
  category: "personal" | "medical" | "school";
  type: string;
  url: string;
  year: string;
}

const documentCategory = {
  personal: "Documentos pessoais",
  medical: "Documentos médicos",
  school: "Documentos escolares",
};

export default function FileViewer() {
  const router = useRouter();
  const params = useParams();
  const patientId = params?.id as string;
  const category = params?.type as "personal" | "medical" | "school";
  const [yearFilter, setYearFilter] = React.useState<string>(
    new Date().getFullYear().toString()
  );
  const [typeFilter, setTypeFilter] = React.useState<string>("LAUDO");
  const [files, setFiles] = React.useState<FileItem[]>([]);

  React.useEffect(() => {
    async function fetchDocuments() {
      try {
        if (!patientId || !category || !yearFilter) return;

        const params = new URLSearchParams({
          category: category,
          year: yearFilter, 
          ...(typeFilter && { type: typeFilter }), 
        });

        const response = await fetch(
          `/api/pessoa/${patientId}/documents?${params.toString()}`
        );
        
        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || "Erro ao buscar os documentos");
        }

        const data = await response.json();
        setFiles(data.urls); 

      } catch (err: any) {
        console.error("Erro ao buscar documentos:", err);
        toast.error(err.message);
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
          <Button
            variant="ghost"
            size="icon"
            onClick={() => router.back()}
            className="flex items-center justify-center"
          >
            <ArrowLeft size={20} />
          </Button>
          
          <h1 className="text-xl font-bold">{documentCategory[category]}</h1>
        </div>
      </div>
      <div className="hidden md:flex items-center justify-center bg-white rounded-4xl shadow-md py-3 px-8 max-w-4xl mx-auto mb-10 gap-12 border text-[#0d4f97]">
        <Button variant="ghost" size="icon" onClick={() => router.back()}>
          <ArrowLeft size={20} />
        </Button>
        
        <h1 className="text-xl font-bold whitespace-nowrap">
          {documentCategory[category]}
        </h1>
      </div>

      <div className="grid grid-cols-2 lg:grid-cols-6 gap-4 mt-4">
        {files.length === 0 ? (
          <p>Nenhum arquivo encontrado.</p>
        ) : (
          files.map((file: any, index: number) => (
            <FileCard
              key={index}
              file={{ fileName: file.fileName, link: file.link }}
            />
          ))
        )}
      </div>
    </main>
  );
}
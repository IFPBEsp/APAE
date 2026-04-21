"use client";

import * as React from "react";
import { useRouter, useParams } from "next/navigation";
import { ArrowLeft, FileText } from "lucide-react";
import { Button } from "@/components/ui/button";
import FileCard from "@/components/fileCard";
import { toast } from "react-toastify";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { file } from "zod";

const documentTypeTranslations: Record<string, string> = {
  MEDICAL_REPORT: "Laudo Médico",
  REFERRAL: "Encaminhamento",
  PRESCRIPTION: "Receita Médica",
  EXAM: "Exame",
  VACCINE_CARD: "Cartão de Vacina",
  PERSONAL_DOCUMENT: "Documento Pessoal",
  SCHOOL_DOCUMENT: "Documento Escolar",
  PHOTO: "Foto",
  EXAMINATION: "Exame", 
  OTHER: "Outro",
};

const translateDocumentType = (typeOrName: string) => {
  if (!typeOrName) return "Documento";
  const cleanType = typeOrName.split(".")[0];
  return documentTypeTranslations[cleanType] || cleanType;
};

export interface FileItem {
  id: string;
  name: string;
  category: "PERSONAL" | "MEDICAL" | "SCHOOL" | "APAE";
  type: string;
  url: string;
  year: string;
}

const documentCategory = {
  pessoal: "Documentos pessoais",
  medico: "Documentos médicos",
  medicos: "Documentos médicos",
  escolar: "Documentos escolares",
  escolares: "Documentos escolares", 
};

interface FileCardProps {
  file: {
    fileName: string;
    link: string;
    originalName?: string; 
  };
}

export default function DocumentTypePage() {
  const router = useRouter();
  const params = useParams();
  const patientId = params?.id as string;

  const isPdf = 
    file.link?.toLowerCase().includes(".pdf") || 
    file.originalName?.toLowerCase().includes(".pdf");

  const category = params?.type as keyof typeof documentCategory;

  const [yearFilter, _setYearFilter] = React.useState<string>(
    new Date().getFullYear().toString()
  );
  const [typeFilter, _setTypeFilter] = React.useState<string>("");
  const [files, setFiles] = React.useState<FileItem[]>([]);

  // Gerar lista de anos (últimos 5 anos + ano atual + próximo ano)
  const availableYears = React.useMemo(() => {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let i = currentYear + 1; i >= currentYear - 5; i--) {
      years.push(i.toString());
    }
    return years;
  }, []);

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
          `/api/pessoas/${patientId}/documentos?${params.toString()}`
        );

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || "Erro ao buscar os documentos");
        }

        const data = await response.json();

        setFiles(data);
      } catch (err: unknown) {
        console.error("Erro ao buscar documentos:", err);
        const errorMessage = err instanceof Error ? err.message : "Erro ao processar resposta da API";
        toast.error(errorMessage);
      }
    }

    fetchDocuments();
  }, [patientId, category, yearFilter, typeFilter]);

  const brandColor = "text-[#0d4f97]";
  const pageTitle = documentCategory[category] || "Documentos";

  return (
    <main className="pt-6 md:pt-12 px-4 py-6 max-w-7xl mx-auto font-baloo">
      <div
        className={`flex flex-col mt-6 md:hidden ${brandColor} items-center`}
      >
        <div className="flex items-center justify-between bg-white rounded-4xl shadow-md mb-4 gap-4 border w-full p-4">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => router.back()}
            className="flex items-center justify-center"
          >
            <ArrowLeft size={20} />
          </Button>
          <h1 className="text-xl font-bold">{pageTitle}</h1>
          <Select value={yearFilter} onValueChange={setYearFilter}>
            <SelectTrigger className="w-20 bg-white border-gray-300 text-sm">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {availableYears.map((year) => (
                <SelectItem key={year} value={year}>{year}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      {/* --- Cabeçalho (Desktop) --- */}
      <div className="hidden md:flex items-center justify-center bg-white rounded-4xl shadow-md py-3 px-8 max-w-4xl mx-auto mb-10 gap-12 border text-[#0d4f97]">
        <Button variant="ghost" size="icon" onClick={() => router.back()}>
          <ArrowLeft size={20} />
        </Button>
        <h1 className="text-xl font-bold whitespace-nowrap">{pageTitle}</h1>
        <div className="flex items-center gap-2">
          <span className="text-sm font-medium text-gray-600">Ano:</span>
          <Select value={yearFilter} onValueChange={setYearFilter}>
            <SelectTrigger className="w-24 bg-white border-gray-300">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {availableYears.map((year) => (
                <SelectItem key={year} value={year}>{year}</SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

     {/* --- Lista de Arquivos --- */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-6 gap-4 mt-4">
        {files.length === 0 ? (
          <p className="col-span-full text-center text-gray-500">
            Nenhum arquivo encontrado.
          </p>
        ) : (
          files.map((file: FileItem) => (
            <FileCard
              key={file.id}
              file={{
                fileName: translateDocumentType(file.type || file.name),
                link: file.url,
                // ADICIONE ESTAS DUAS LINHAS ABAIXO:
                originalName: file.name,
                type: file.type
              }}
            />
          ))
        )}
      </div>
    </main>
  );
}
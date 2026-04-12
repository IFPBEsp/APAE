"use client";

import * as React from "react";
import { useRouter, useParams } from "next/navigation";
import { ArrowLeft, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import FileCard from "@/components/fileCard";
import { toast } from "react-toastify";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";

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

export default function DocumentTypePage() {
  const router = useRouter();
  const params = useParams();
  const patientId = params?.id as string;
  const category = params?.type as keyof typeof documentCategory;

  // Estados
  const [yearFilter, setYearFilter] = React.useState<string>(
    new Date().getFullYear().toString()
  );
  const [typeFilter, setTypeFilter] = React.useState<string>("");
  const [files, setFiles] = React.useState<FileItem[]>([]);
  
  // Estado responsável por controlar o Preview (Modal)
  const [selectedFile, setSelectedFile] = React.useState<FileItem | null>(null);

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
      } catch (err: any) {
        console.error("Erro ao buscar documentos:", err);
        toast.error(err.message || "Erro ao processar resposta da API");
        setFiles([]);
      }
    }

    fetchDocuments();
  }, [patientId, category, yearFilter, typeFilter]);

  const brandColor = "text-[#0d4f97]";
  const pageTitle = documentCategory[category] || "Documentos";

  return (
    <main className="pt-6 md:pt-12 px-4 py-6 max-w-7xl mx-auto font-baloo relative">
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
      <div className="grid grid-cols-2 lg:grid-cols-6 gap-4 mt-4">
        {files.length === 0 ? (
          <p className="col-span-full text-center text-gray-500">
            Nenhum arquivo encontrado.
          </p>
        ) : (
          files.map((file: FileItem) => (
            <div 
              key={file.id} 
              onClickCapture={(e) => {
                e.preventDefault();
                e.stopPropagation(); // Impede outras ações do clique padrão
                setSelectedFile(file); // Abre o Preview Modal
              }}
              className="cursor-pointer transition-transform hover:scale-105"
            >
              <FileCard
                file={{
                  fileName: translateDocumentType(file.type || file.name),
                  link: file.url,
                }}
              />
            </div>
          ))
        )}
      </div>

      {/* --- MODAL DE PREVIEW DO DOCUMENTO --- */}
      {selectedFile && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm p-4">
          <div className="bg-white rounded-xl shadow-2xl w-full max-w-5xl h-[85vh] flex flex-col overflow-hidden">
            
            <div className="flex justify-between items-center p-4 border-b bg-gray-50">
              <h2 className="text-lg font-bold text-[#0d4f97]">
                Preview: {translateDocumentType(selectedFile.type || selectedFile.name)}
              </h2>
              <div className="flex gap-4 items-center">
                <a 
                  href={selectedFile.url} 
                  target="_blank" 
                  rel="noopener noreferrer"
                  className="text-sm font-semibold text-[#0d4f97] hover:underline bg-blue-50 px-3 py-1 rounded"
                >
                  Abrir em nova aba
                </a>
                <Button variant="ghost" size="icon" onClick={() => setSelectedFile(null)} className="hover:bg-red-100">
                  <X size={24} className="text-white bg-red-700 border-red-700 border rounded-2xl" />
                </Button>
              </div>
            </div>

            <div className="flex-1 bg-gray-200 flex items-center justify-center p-2 overflow-auto">
              {/* Lógica invertida robusta: Se for imagem óbvia, usa <img>. Senão, força o <iframe> */}
              {selectedFile.url?.toLowerCase().match(/\.(jpeg|jpg|gif|png)$/) != null ? (
                <img 
                  src={selectedFile.url} 
                  alt="Documento Preview" 
                  className="max-w-full max-h-full object-contain rounded shadow-sm"
                />
              ) : (
                <iframe 
                  src={selectedFile.url} 
                  className="w-full h-full rounded shadow-sm bg-white"
                  title="PDF Preview"
                />
              )}
            </div>
          </div>
        </div>
      )}
    </main>
  );
}
"use client";

import * as React from "react";
import { useRouter, useParams } from "next/navigation";
import { ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import FileCard from "@/components/fileCard";
import { toast } from "react-toastify";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

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

  const [yearFilter, _setYearFilter] = React.useState<string>(
    new Date().getFullYear().toString(),
  );
  const [typeFilter, _setTypeFilter] = React.useState<string>("");
  const [files, setFiles] = React.useState<FileItem[]>([]);
  const [replaceOpen, setReplaceOpen] = React.useState(false);
  const [replaceTarget, setReplaceTarget] = React.useState<FileItem | null>(
    null,
  );
  const [replaceFile, setReplaceFile] = React.useState<File | null>(null);
  const [replaceLoading, setReplaceLoading] = React.useState(false);

  // Gerar lista de anos (últimos 5 anos + ano atual + próximo ano)
  const availableYears = React.useMemo(() => {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let i = currentYear + 1; i >= currentYear - 5; i--) {
      years.push(i.toString());
    }
    return years;
  }, []);

  const fetchDocuments = React.useCallback(async () => {
    try {
      if (!patientId || !category || !yearFilter) return;

      const params = new URLSearchParams({
        category: category,
        year: yearFilter,
        ...(typeFilter && { type: typeFilter }),
      });

      const response = await fetch(
        `/api/pessoas/${patientId}/documentos?${params.toString()}`,
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao buscar os documentos");
      }

      const data = await response.json();

      setFiles(data);
    } catch (err: unknown) {
      console.error("Erro ao buscar documentos:", err);
      const errorMessage =
        err instanceof Error
          ? err.message
          : "Erro ao processar resposta da API";
      toast.error(errorMessage);
    }
  }, [patientId, category, yearFilter, typeFilter]);

  React.useEffect(() => {
    fetchDocuments();
  }, [fetchDocuments]);

  const brandColor = "text-[#0d4f97]";
  const pageTitle = documentCategory[category] || "Documentos";
  const isReplaceableDocument = (file: FileItem) =>
    file.category === "MEDICAL" &&
    ["MEDICAL_REPORT", "REFERRAL"].includes(file.type);
  const replaceTargetLabel = replaceTarget
    ? translateDocumentType(replaceTarget.type || replaceTarget.name)
    : "Documento";
  const selectedFileName = replaceFile?.name ?? "Nenhum arquivo selecionado";

  const openReplaceModal = (file: FileItem) => {
    setReplaceTarget(file);
    setReplaceFile(null);
    setReplaceOpen(true);
  };

  const closeReplaceModal = () => {
    setReplaceOpen(false);
    setReplaceTarget(null);
    setReplaceFile(null);
  };

  const confirmReplace = async () => {
    if (!patientId || !replaceTarget || !replaceFile) {
      toast.error("Selecione um arquivo para substituir.");
      return;
    }

    try {
      setReplaceLoading(true);
      const formData = new FormData();
      formData.append("file", replaceFile);

      const response = await fetch(
        `/api/pessoas/${patientId}/documentos/${replaceTarget.id}`,
        {
          method: "PATCH",
          body: formData,
        },
      );

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || "Erro ao substituir o documento");
      }

      toast.success("Documento substituído com sucesso.");
      closeReplaceModal();
      await fetchDocuments();
    } catch (error: unknown) {
      const message =
        error instanceof Error
          ? error.message
          : "Erro ao substituir o documento";
      toast.error(message);
    } finally {
      setReplaceLoading(false);
    }
  };

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
          <Select value={yearFilter} onValueChange={_setYearFilter}>
            <SelectTrigger className="w-20 bg-white border-gray-300 text-sm">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {availableYears.map((year) => (
                <SelectItem key={year} value={year}>
                  {year}
                </SelectItem>
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
          <Select value={yearFilter} onValueChange={_setYearFilter}>
            <SelectTrigger className="w-24 bg-white border-gray-300">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {availableYears.map((year) => (
                <SelectItem key={year} value={year}>
                  {year}
                </SelectItem>
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
                originalName: file.name,
                type: file.type,
              }}
              canReplace={isReplaceableDocument(file)}
              onReplace={
                isReplaceableDocument(file)
                  ? () => openReplaceModal(file)
                  : undefined
              }
            />
          ))
        )}
      </div>

      <Dialog
        open={replaceOpen}
        onOpenChange={(open) => {
          if (replaceLoading) return;
          if (open) setReplaceOpen(true);
          else closeReplaceModal();
        }}
      >
        <DialogContent className="sm:max-w-[680px]">
          <DialogHeader>
            <DialogTitle>Substituir documento</DialogTitle>
            <DialogDescription>
              Selecione um novo arquivo para substituir {replaceTargetLabel}. A
              troca só será concluída se o novo upload e a remoção do anterior
              ocorrerem com sucesso.
            </DialogDescription>
          </DialogHeader>

          <div className="grid gap-4 md:grid-cols-[1.15fr_0.85fr]">
            <div className="space-y-3 rounded-2xl border bg-slate-50 p-4">
              <p className="text-sm font-semibold text-slate-700">
                Documento alvo
              </p>
              <div className="rounded-xl border bg-white p-3 shadow-sm">
                <p className="text-base font-semibold text-slate-900">
                  {replaceTargetLabel}
                </p>
                <p className="text-xs text-slate-500 break-all">
                  {replaceTarget?.name ?? "Documento atual"}
                </p>
              </div>

              <div>
                <p className="text-sm font-medium text-gray-700 mb-2">
                  Arquivo novo
                </p>
                <Input
                  type="file"
                  accept="application/pdf,image/*"
                  onChange={(event) =>
                    setReplaceFile(event.target.files?.[0] ?? null)
                  }
                />
                <p className="mt-2 text-xs text-slate-500">
                  Arquivos PDF e imagens são aceitos.
                </p>
              </div>
            </div>

            <div className="rounded-2xl border border-dashed border-slate-200 bg-white p-4">
              <p className="text-sm font-semibold text-slate-700">
                Arquivo selecionado
              </p>
              <div className="mt-3 rounded-xl bg-slate-50 p-3">
                <p className="text-sm font-medium text-slate-900 break-all">
                  {selectedFileName}
                </p>
                <p className="mt-1 text-xs text-slate-500">
                  Confirme apenas depois de revisar o arquivo escolhido.
                </p>
              </div>
            </div>
          </div>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={closeReplaceModal}
              disabled={replaceLoading}
            >
              Cancelar
            </Button>
            <Button
              type="button"
              onClick={confirmReplace}
              disabled={replaceLoading || !replaceFile}
            >
              {replaceLoading ? "Substituindo..." : "Confirmar substituição"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </main>
  );
}

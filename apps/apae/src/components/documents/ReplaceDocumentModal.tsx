"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

type ReplaceDocumentModalProps = {
  open: boolean;
  loading: boolean;
  documentLabel: string;
  documentName?: string;
  selectedFile: File | null;
  onOpenChange: (open: boolean) => void;
  onSelectedFileChange: (file: File | null) => void;
  onConfirm: () => void;
};

export default function ReplaceDocumentModal({
  open,
  loading,
  documentLabel,
  documentName,
  selectedFile,
  onOpenChange,
  onSelectedFileChange,
  onConfirm,
}: ReplaceDocumentModalProps) {
  const selectedFileName = selectedFile?.name ?? "Nenhum arquivo selecionado";

  return (
    <Dialog
      open={open}
      onOpenChange={(nextOpen) => {
        if (loading) return;
        onOpenChange(nextOpen);
      }}
    >
      <DialogContent className="sm:max-w-[680px]">
        <DialogHeader>
          <DialogTitle>Substituir documento</DialogTitle>
          <DialogDescription>
            Selecione um novo arquivo para substituir {documentLabel}. A troca só será concluída se
            o novo upload e a remoção do anterior ocorrerem com sucesso.
          </DialogDescription>
        </DialogHeader>

        <div className="grid gap-4 md:grid-cols-[1.15fr_0.85fr]">
          <div className="space-y-3 rounded-2xl border bg-slate-50 p-4">
            <p className="text-sm font-semibold text-slate-700">Documento alvo</p>
            <div className="rounded-xl border bg-white p-3 shadow-sm">
              <p className="text-base font-semibold text-slate-900">{documentLabel}</p>
              <p className="text-xs text-slate-500 break-all">
                {documentName ?? "Documento atual"}
              </p>
            </div>

            <div>
              <p className="text-sm font-medium text-gray-700 mb-2">Arquivo novo</p>
              <Input
                type="file"
                accept="application/pdf,image/*"
                onChange={(event) => onSelectedFileChange(event.target.files?.[0] ?? null)}
              />
              <p className="mt-2 text-xs text-slate-500">Arquivos PDF e imagens são aceitos.</p>
            </div>
          </div>

          <div className="rounded-2xl border border-dashed border-slate-200 bg-white p-4">
            <p className="text-sm font-semibold text-slate-700">Arquivo selecionado</p>
            <div className="mt-3 rounded-xl bg-slate-50 p-3">
              <p className="text-sm font-medium text-slate-900 break-all">{selectedFileName}</p>
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
            onClick={() => onOpenChange(false)}
            disabled={loading}
          >
            Cancelar
          </Button>
          <Button type="button" onClick={onConfirm} disabled={loading || !selectedFile}>
            {loading ? "Substituindo..." : "Confirmar substituição"}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

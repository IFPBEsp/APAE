"use client";

import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { FormControl, FormItem, FormLabel } from "@/components/ui/form";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";
import { DocumentWithUrl } from "@/types/document";

interface ProfessionalDocumentsProps {
  groupedDocs: {
    curriculum?: DocumentWithUrl;
    volunteer?: DocumentWithUrl;
    attachments: DocumentWithUrl[];
  };
  docsLoading: boolean;
  docsError: string | null;
  removingIds: Set<string>;
  removeModalOpen: boolean;
  setRemoveModalOpen: (open: boolean) => void;
  docToRemove: DocumentWithUrl | null;
  isConfirmBusy: boolean;
  openRemoveModal: (doc: DocumentWithUrl) => void;
  confirmRemove: () => void;
  curriculumFile: File | null;
  volunteerFile: File | null;
  attachmentFiles: File[];
  setCurriculumFile: (f: File | null) => void;
  setVolunteerFile: (f: File | null) => void;
  setAttachmentFiles: (f: File[]) => void;
  isValidFile: (f: File) => boolean;
  errorDocs?: string | null;
  successDocs?: boolean;
  hasAnyUpload: boolean;
  loadingDocs?: boolean;
}

export function ProfessionalDocuments({
  groupedDocs,
  docsLoading,
  docsError,
  removingIds,
  removeModalOpen,
  setRemoveModalOpen,
  docToRemove,
  isConfirmBusy,
  openRemoveModal,
  confirmRemove,
  curriculumFile,
  volunteerFile,
  attachmentFiles,
  setCurriculumFile,
  setVolunteerFile,
  setAttachmentFiles,
  isValidFile,
  errorDocs,
  successDocs,
  hasAnyUpload,
  loadingDocs,
}: ProfessionalDocumentsProps) {
  return (
    <div className="space-y-4">
      <AlertDialog open={removeModalOpen} onOpenChange={setRemoveModalOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Você tem certeza?</AlertDialogTitle>
            <AlertDialogDescription>
              Esta ação irá remover o anexo{" "}
              <span className="font-medium">{docToRemove?.name ?? "selecionado"}</span>
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={isConfirmBusy}>Cancelar</AlertDialogCancel>
            <AlertDialogAction
              className="border border-input bg-background text-foreground hover:bg-accent hover:text-accent-foreground"
              onClick={confirmRemove}
              disabled={isConfirmBusy}
            >
              {isConfirmBusy ? "Removendo..." : "Remover"}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <div className="rounded-md border p-4 space-y-2">
        <p className="text-base font-semibold">Documentos já anexados</p>
        {docsLoading ? (
          <p className="text-sm text-gray-600">Carregando documentos...</p>
        ) : docsError ? (
          <p className="text-sm text-red-500">{docsError}</p>
        ) : (
          <div className="text-sm text-gray-700 space-y-2">
            <p>
              <span className="font-medium">Termo do voluntário: </span>
              {groupedDocs.volunteer ? (
                <a
                  className="text-[#0D4F97] hover:underline"
                  href={groupedDocs.volunteer.url}
                  target="_blank"
                  rel="noreferrer"
                >
                  Visualizar
                </a>
              ) : (
                <span className="text-gray-500">não enviado</span>
              )}
            </p>
            <p>
              <span className="font-medium">Currículo: </span>
              {groupedDocs.curriculum ? (
                <a
                  className="text-[#0D4F97] hover:underline"
                  href={groupedDocs.curriculum.url}
                  target="_blank"
                  rel="noreferrer"
                >
                  Visualizar
                </a>
              ) : (
                <span className="text-gray-500">não enviado</span>
              )}
            </p>
            <div>
              <p>
                <span className="font-medium">Anexos: </span>
                {groupedDocs.attachments.length > 0 ? (
                  <span>{groupedDocs.attachments.length} arquivo(s)</span>
                ) : (
                  <span className="text-gray-500">nenhum</span>
                )}
              </p>
              {groupedDocs.attachments.length > 0 && (
                <ul className="mt-2 space-y-2">
                  {groupedDocs.attachments.map((a) => {
                    const busy = removingIds.has(String(a.id));
                    return (
                      <li
                        key={a.id}
                        className="flex items-center justify-between rounded-md border px-3 py-2"
                      >
                        <p className="truncate text-sm text-gray-700">{a.name}</p>
                        <div className="flex items-center gap-2">
                          <a
                            className="text-[#0D4F97] hover:underline text-sm"
                            href={a.url}
                            target="_blank"
                            rel="noreferrer"
                          >
                            Visualizar
                          </a>
                          <Button
                            type="button"
                            variant="outline"
                            size="icon"
                            className="h-8 w-8 text-muted-foreground hover:text-foreground"
                            disabled={busy || loadingDocs || docsLoading}
                            onClick={() => openRemoveModal(a)}
                          >
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </li>
                    );
                  })}
                </ul>
              )}
            </div>
          </div>
        )}
      </div>

      <FormItem>
        <FormLabel>Termo do Voluntário</FormLabel>
        <FormControl>
          <Input
            type="file"
            accept="image/*, application/pdf"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (!file) {
                setVolunteerFile(null);
                return;
              }
              if (!isValidFile(file)) {
                alert("Apenas imagens ou PDF são permitidos");
                e.target.value = "";
                setVolunteerFile(null);
                return;
              }
              setVolunteerFile(file);
            }}
          />
        </FormControl>
        {volunteerFile && (
          <p className="text-xs text-gray-600 mt-1">Selecionado: {volunteerFile.name}</p>
        )}
      </FormItem>

      <FormItem>
        <FormLabel>Currículo</FormLabel>
        <FormControl>
          <Input
            type="file"
            accept="image/*, application/pdf"
            onChange={(e) => {
              const file = e.target.files?.[0];
              if (!file) {
                setCurriculumFile(null);
                return;
              }
              if (!isValidFile(file)) {
                alert("Apenas imagens ou PDF são permitidos");
                e.target.value = "";
                setCurriculumFile(null);
                return;
              }
              setCurriculumFile(file);
            }}
          />
        </FormControl>
        {curriculumFile && (
          <p className="text-xs text-gray-600 mt-1">Selecionado: {curriculumFile.name}</p>
        )}
      </FormItem>

      <FormItem>
        <FormLabel>Anexo qualquer</FormLabel>
        <FormControl>
          <Input
            type="file"
            accept="image/*, application/pdf"
            multiple
            onChange={(e) => {
              const files = Array.from(e.target.files ?? []);
              const valid = files.filter(isValidFile);
              if (valid.length !== files.length)
                alert("Alguns arquivos foram ignorados. Apenas imagens ou PDF são permitidos.");
              setAttachmentFiles(valid);
            }}
          />
        </FormControl>
        {attachmentFiles.length > 0 && (
          <p className="text-xs text-gray-600 mt-1">
            Selecionado(s): {attachmentFiles.length} arquivo(s)
          </p>
        )}
      </FormItem>

      {(errorDocs || successDocs) && (
        <div className="text-sm">
          {errorDocs && <p className="text-red-500">{errorDocs}</p>}
          {successDocs && <p className="text-green-600">Documentos enviados com sucesso!</p>}
        </div>
      )}

      {hasAnyUpload && (
        <p className="text-xs text-gray-600">
          Ao salvar, também serão enviados os documentos selecionados.
        </p>
      )}
    </div>
  );
}

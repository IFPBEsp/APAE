"use client";

import * as React from "react";
import { FileText, ExternalLink } from "lucide-react";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";

interface FileCardProps {
  file: {
    fileName: string;
    link: string;
    type?: string;
    originalName?: string;
  };
  canReplace?: boolean;
  onReplace?: () => void;
}

export default function FileCard({ file, canReplace = false, onReplace }: FileCardProps) {
  const [open, setOpen] = React.useState(false);
  const [modalViewType, setModalViewType] = React.useState<"img" | "iframe">("img");
  const [thumbnailView, setThumbnailView] = React.useState<"img" | "icon">("img");

  React.useEffect(() => {
    if (open) setModalViewType("img");
  }, [open]);

  return (
    <>
      <div
        className="cursor-pointer p-3 border rounded hover:bg-gray-100 overflow-hidden flex flex-col"
        onClick={() => setOpen(true)}
      >
        <p className="truncate mb-2 text-sm font-medium">{file.fileName}</p>

        <div className="flex flex-col items-center justify-center bg-blue-50 text-blue-900 rounded-md w-full h-32 border border-blue-100 overflow-hidden relative">
          {thumbnailView === "img" ? (
            <img
              src={file.link}
              alt={file.fileName}
              className="w-full h-full object-cover"
              onError={() => setThumbnailView("icon")}
            />
          ) : (
            <>
              <FileText size={40} strokeWidth={1.5} className="mb-1 text-blue-800" />
              <span className="text-[10px] font-bold uppercase tracking-wider text-blue-800">
                Documento
              </span>
            </>
          )}
        </div>

        {canReplace && onReplace && (
          <Button
            type="button"
            variant="secondary"
            className="mt-3 w-full border border-blue-100 bg-blue-50 text-blue-900 hover:bg-blue-100"
            onClick={(e) => {
              e.stopPropagation();
              onReplace();
            }}
          >
            Substituir
          </Button>
        )}
      </div>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-[900px] max-h-[90vh] overflow-hidden bg-white">
          <DialogHeader className="flex flex-row items-center justify-between">
            <DialogTitle className="truncate pr-4">{file.fileName}</DialogTitle>
          </DialogHeader>

          <div className="flex justify-center items-center w-full h-[60vh] bg-gray-50 rounded-lg overflow-hidden relative">
            {modalViewType === "img" && (
              <img
                src={file.link}
                alt={file.fileName}
                className="w-full h-full object-contain"
                onError={() => setModalViewType("iframe")}
              />
            )}
            {modalViewType === "iframe" && (
              <iframe src={file.link} title={file.fileName} className="w-full h-full border-0" />
            )}
          </div>

          <div className="flex justify-center mt-4 pb-2">
            <Button
              variant="default"
              onClick={() => window.open(file.link, "_blank")}
              className="gap-2"
            >
              <ExternalLink size={18} />
              Abrir em nova guia
            </Button>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
}

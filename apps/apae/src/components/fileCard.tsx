"use client";

import Image from "next/image";
import { useState } from "react";

import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Button } from "./ui/button";

interface Props {
  file: {
    fileName: string;
    link: string;
  };
}

export default function FileCard({ file }: Props) {
  const [open, setOpen] = useState(false);
  const descriptionId = `dialog-description-${file.fileName.replace(/\s+/g, "-")}`;

  return (
    <>
      <div
        className="cursor-pointer p-3 border rounded hover:bg-gray-100 overflow-hidden"
        onClick={() => setOpen(true)}
      >
        <p className="truncate">{file.fileName}</p>
        <div className="relative w-full h-32 mt-2">
          <Image
            src={file.link}
            alt={file.fileName}
            fill
            priority
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
            className="object-contain"
          />
        </div>
      </div>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent
          className="sm:max-w-[800px]"
          aria-describedby={descriptionId}
        >
          <DialogHeader>
            <DialogTitle>{file.fileName}</DialogTitle>
          </DialogHeader>
          <div
            id={descriptionId}
            className="relative w-full h-[60vh] flex justify-center"
          >
            <Image
              src={file.link}
              alt={file.fileName}
              fill
              sizes="(max-width: 800px) 100vw, 800px"
              className="object-contain"
            />
          </div>
          <DialogFooter>
            <Button
              variant="secondary"
              onClick={() => window.open(file.link, "_blank")}
              className="cursor-pointer hover:bg-gray-300 hover:text-black transition-colors"
            >
              Visualizar Arquivo
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}

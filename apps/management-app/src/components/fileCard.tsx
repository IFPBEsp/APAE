"use client";

import * as React from "react";
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
  const [open, setOpen] = React.useState(false);

  return (
    <>
      <div
        className="cursor-pointer p-3 border rounded hover:bg-gray-100"
        onClick={() => setOpen(true)}
      >
        <p className="truncate">{file.fileName}</p>
        <img
          src={file.link}
          alt={file.fileName}
          className="w-full h-32 object-contain mt-2"
        />
      </div>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="sm:max-w-[800px]">
          <DialogHeader>
            <DialogTitle>{file.fileName}</DialogTitle>
          </DialogHeader>
          <div className="flex justify-center">
            <img
              src={file.link}
              alt={file.fileName}
              className="max-w-full max-h-[80vh] object-contain"
            />
          </div>
          <DialogFooter>
            <Button
              variant="secondary"
              onClick={() => window.open(file.link, file.fileName)}
            >
              Instalar o arquivo
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </>
  );
}

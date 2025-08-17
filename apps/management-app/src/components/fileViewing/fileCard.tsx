"use client";

import * as React from "react";

interface Props {
  file: {
    fileName: string;
    link: string;
  };
}

export default function FileCard({ file }: Props) {
  const openFile = () => {
    window.open(file.link, "_blank");
  };

  return (
    <div
      className="cursor-pointer p-3 border rounded hover:bg-gray-100"
      onClick={openFile}
    >
      <p className="truncate">{file.fileName}</p>
    </div>
  );
}


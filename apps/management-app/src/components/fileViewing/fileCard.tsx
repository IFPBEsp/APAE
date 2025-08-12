import * as React from "react";
import { viewDocumentAsImage } from "@/service/fileViewingService";

interface FileCardProps {
  name: string;
  url: string;
  patientId: string;
}

export default function FileCard({ name, url, patientId }: FileCardProps) {
  const [imageSrc, setImageSrc] = React.useState<string | null>(null);

  React.useEffect(() => {
    async function fetchImage() {
      try {
        const blobUrl = await viewDocumentAsImage(patientId, url);
        setImageSrc(blobUrl);
      } catch (err) {
        console.error("Erro ao carregar imagem:", err);
      }
    }

    fetchImage();
  }, [url, patientId]);

  return (
    <div className="bg-white border rounded-lg p-2 shadow text-center text-sm">
      {imageSrc ? (
        <img src={imageSrc} alt={name} className="w-full h-32 object-cover rounded-md mb-2" />
      ) : (
        <div className="w-full h-32 bg-gray-200 animate-pulse rounded-md mb-2" />
      )}
      <p className="truncate">{name}</p>
    </div>
  );
}

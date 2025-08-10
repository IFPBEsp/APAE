import { Card } from "@/components/ui/card";
import {FileUp} from "lucide-react";

type FileCardProps = {
  name: string;
  url: string;
};

export default function FileCard({ name, url }: FileCardProps) {
    const openFile = () => {
        window.open(url, "_blank");
    };
    
    return (
    <Card 
        className="flex flex-col items-center p-4 hover:shadow-lg transition cursor-pointer" 
        onClick={openFile}      
        role="button"
        title={`Abrir ${name}`}>
    <FileUp className="w-16 h-16 text-gray-700"></FileUp>
    <span className="mt-2 text-sm text-gray-600 break-words">{name}</span>
    </Card>
  );
}

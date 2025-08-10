import FileViewer from "@/components/fileViewing";

export default function FileViewerPage() {
  return (
    <main className="p-6 max-w-7xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Visualização de Arquivos</h1>
      <FileViewer />
    </main>
  );
}

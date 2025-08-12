import FileViewer from "@/components/fileViewing";

export default function FileViewerPage() {
  return (
    <main className="p-6 max-w-7xl mx-auto">
      <FileViewer initialCategory={"escolar"} />
    </main>
  );
}

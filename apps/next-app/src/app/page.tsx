import ApaeLayout from "@/components/apae-layout";

export default function Page() {
  return (
    <ApaeLayout title="Inicial">
      <div className="grid gap-4 md:grid-cols-2">
        <div className="h-40 rounded-lg bg-muted" />
        <div className="h-40 rounded-lg bg-muted" />
      </div>
    </ApaeLayout>
  );
}

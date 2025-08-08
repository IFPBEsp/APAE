import ApaeLayout from "@/app/components/apae-layout"

export default function Page() {
  return (
    <ApaeLayout title="Dashboard">
      <div className="grid gap-4 md:grid-cols-3">
        <div className="h-40 rounded-lg bg-muted" />
        <div className="h-40 rounded-lg bg-muted" />
        <div className="h-40 rounded-lg bg-muted" />
      </div>
    </ApaeLayout>
  )
}

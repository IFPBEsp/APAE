import { VaccinesProvider } from "@/hooks/use-vaccines";
import { VaccinesLayoutClient } from "./layout-client";

export default function VaccinesLayout({ children }: { readonly children: React.ReactNode }) {
  return (
    <VaccinesProvider>
      <VaccinesLayoutClient>{children}</VaccinesLayoutClient>
    </VaccinesProvider>
  );
}

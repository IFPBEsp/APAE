import { VaccinesProvider } from "@/domains/vaccines/use-vaccines";
import { VaccinesLayoutClient } from "@/domains/vaccines/shared/layout-client";

export default function VaccinesLayout({
    children,
}: {
    readonly children: React.ReactNode;
}) {
    return (
        <VaccinesProvider>
            <VaccinesLayoutClient>{children}</VaccinesLayoutClient>
        </VaccinesProvider>
    );
}

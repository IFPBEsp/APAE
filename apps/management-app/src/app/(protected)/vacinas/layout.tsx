import { VaccinesProvider } from "@/hooks/use-vaccines";

export default function VaccinesLayout({
    children,
}: {
    readonly children: React.ReactNode;
}) {
    return <VaccinesProvider>{children}</VaccinesProvider>;
}

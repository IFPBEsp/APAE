import Image from "@/app/assets/background_image.jpg"
import { PageOrchestrator } from "./orchestrator";
import { Nunito } from "next/font/google";
import { MembersRegisterProvider } from "@/hooks/use-members-register-context";

const nunito = Nunito({
  subsets: ["latin"],
  weight: ["400", "600", "700", "900"],
});

export default function MembersRegisterLayout({
  children,}: {
  readonly children: React.ReactNode;
}) {
  return (
    <MembersRegisterProvider>
      <PageOrchestrator>
        <div className="h-screen rounded-lg mx-10 relative grid grid-cols-1 md:grid-cols-[1fr_2fr] antialiased overflow-hidden">
          <div
            className="absolute inset-0 bg-cover bg-center bg-no-repeat grayscale-90"
            style={{
              backgroundImage: `url(${Image.src})`,
              backgroundAttachment: "fixed",
            }}
          />

          <div
            className="absolute inset-0"
            style={{
              background:
                "linear-gradient(180deg, rgba(13, 79, 151, 0.7) 54.32%, rgba(255, 255, 255, 0.6) 110.28%)",
            }}
          />
          <div
            className={`relative flex flex-col w-full h-full justify-center text-center text-white font-bold ${nunito.className}`}
          >
            <h1 className="text-4xl">BEM-VINDO</h1>
            <p className="text-3xl px-28 py-12">
              Informe seus dados ao lado para poder fazer parte da nossa
              associação.
            </p>
          </div>
          <div className="relative flex flex-col w-full h-full p-8 bg-muted overflow-y-auto">
            <h1 className="text-2xl font-bold text-blue-900 mb-4">
              Cadastro de pessoas
            </h1>
            {children}
          </div>
        </div>
      </PageOrchestrator>
    </MembersRegisterProvider>
  );
}

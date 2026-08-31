import Image from "@/assets/background_image.jpg";
import { Nunito } from "next/font/google";

const nunito = Nunito({
  subsets: ["latin"],
  weight: ["400", "600", "700", "900"],
});

export default function Layout({ children }: { readonly children: React.ReactNode }) {
  return (
    <div className="m-5 rounded-lg mx-6 relative grid grid-cols-1 md:grid-cols-[1fr_2fr] gap-x-4 antialiased overflow-hidden">
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
          Informe seus dados ao lado para poder fazer parte da nossa associação.
        </p>
      </div>

      <div
        className="relative flex flex-col w-full h-full p-8 bg-muted overflow-y-auto "
        style={{ backgroundColor: "#F5F5F5" }}
      >
        <h1 className="text-2xl font-bold text-blue-900 mb-4">Cadastro de Profissional</h1>
        {children}
      </div>
    </div>
  );
}

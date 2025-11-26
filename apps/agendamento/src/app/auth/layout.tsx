import Image from "@/../assets/background_image.jpg";
import Logo from "@/assets/logo.png";

export default function AuthLayout({
  children,
}: {
  readonly children: React.ReactNode;
}) {
  return (
    <div className="h-screen w-screen relative flex items-center justify-center overflow-hidden">
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
            "linear-gradient(180deg, rgba(13, 79, 151, 0.5) 24.32%, rgba(255, 255, 255, 0.5) 99.28%)",
        }}
      />

      <div className="relative z-10 relative flex items-center justify-center p-4">
        <div
          className="absolute top-[-2vh] left-1/2 transform -translate-x-1/2 z-[10]
                w-18 sm:w-18 md:w-20 lg:w-22
                bg-white rounded-t-full flex items-center justify-center overflow-hidden"
        >
          <div className="relative w-full">
            <img
              src={Logo.src}
              alt="Logo"
              className="w-full h-auto object-contain"
            />
          </div>
        </div>
        {children}
      </div>
    </div>
  );
}

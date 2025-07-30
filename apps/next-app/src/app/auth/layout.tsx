import Image from "@/app/assets/background_image.jpg";
import Logo from "@/app/assets/logo.png";

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
          className="absolute top-[0] sm:top-[-4vh] lg:top-[-4vh] left-1/2 transform -translate-x-1/2 z-[10]
            sm:w-[6rem] sm:h-[6rem] sm:w-[8rem] sm:h-[8rem] lg:w-[10rem] lg:h-[10rem]
                bg-white rounded-t-full flex items-center justify-center overflow-auto"
        >
          {/* Group 3 e Image 5 - Imagem centralizada */}
          <div className="relative w-[55px] h-[80px] md:w-[75px] md:h-[105px] lg:w-[85px] lg:h-[115px]">
            <img
              src={Logo.src}
              alt="Logo"
              className=" object-cover w-[55px] h-[77px] md:w-[75px] md:h-[105px] lg:w-[105px] lg:h-[117px]"
            />
          </div>
        </div>
        {children}
      </div>
    </div>
  );
}

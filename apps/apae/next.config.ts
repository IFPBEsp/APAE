import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: "standalone",
  basePath: "/apae-geral",
  async redirects() {
    return [
      { source: "/visualization-patients",     destination: "/patients",               permanent: true },
      { source: "/visualization-professional", destination: "/professionals",          permanent: true },
      { source: "/profissionais/:id",          destination: "/professionals/view/:id", permanent: true },
      { source: "/register-profissional",      destination: "/professionals/create",   permanent: true },
      { source: "/update-profissional/:id",    destination: "/professionals/edit/:id", permanent: true },
      { source: "/person/:path*",              destination: "/patients/:path*",        permanent: true },
      { source: "/tipo-atendimento/:path*",    destination: "/service-types/:path*",   permanent: true },
      { source: "/all-appointments",           destination: "/appointments/list",      permanent: true },
    ];
  },
};

export default nextConfig;
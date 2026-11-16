import { NextRequest, NextResponse } from "next/server";

const PUBLIC_PATHS = ["/auth/login", "/auth/recovery", "/auth/reset-password"];
const ADMIN_ONLY_PATH = "/auth/register";

function getRoleFromSession(session: string): string | null {
  try {
    const { accessToken } = JSON.parse(session);
    const base64Url = accessToken.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64 + "=".repeat((4 - (base64.length % 4)) % 4);
    const payload = JSON.parse(atob(padded));

    return typeof payload.role === "string" ? payload.role : null;
  } catch {
    return null;
  }
}

export function proxy(req: NextRequest) {
  const session = req.cookies.get("session")?.value;
  const isPublic = PUBLIC_PATHS.includes(req.nextUrl.pathname);

  if (!session && !isPublic) {
    const url = req.nextUrl.clone();
    url.pathname = "/auth/login";
    return NextResponse.redirect(url);
  }

  if (session && isPublic) {
    const url = req.nextUrl.clone();
    url.pathname = "/";
    return NextResponse.redirect(url);
  }

  if (
    session &&
    req.nextUrl.pathname.startsWith(ADMIN_ONLY_PATH) &&
    getRoleFromSession(session) !== "ADMIN"
  ) {
    const url = req.nextUrl.clone();
    url.pathname = "/";
    return NextResponse.redirect(url);
  }

  return NextResponse.next();
}

export const config = {
  matcher: [
    "/",
    "/((?!api|_next/static|_next/image|.*\\.png$).*)",
  ],
};

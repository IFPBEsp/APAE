import { NextRequest, NextResponse } from "next/server";

const PUBLIC_PATHS = ["/auth/login", "/auth/recovery", "/auth/reset-password"];

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

  return NextResponse.next();
}

export const config = {
  matcher: [
    "/",
    "/((?!api|_next/static|_next/image|.*\\.png$).*)",
  ],
};

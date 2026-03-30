import { NextRequest, NextResponse } from "next/server";

const PUBLIC_PATHS = ["/auth/login"];

export function middleware(req: NextRequest) {
  const session = req.cookies.get("session")?.value;
  const isPublic = PUBLIC_PATHS.includes(req.nextUrl.pathname);

  if (!session && !isPublic) {
    return NextResponse.redirect(new URL("/auth/login", req.url));
  }

  if (session && isPublic) {
    return NextResponse.redirect(new URL("/", req.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};
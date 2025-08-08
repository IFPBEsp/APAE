import { NextRequest, NextResponse } from "next/server";

const PUBLIC_PATHS = ["/login", "/register"];

export function middleware(req: NextRequest) {
  const session = req.cookies.get("session")?.value;
  console.log(session);
  const isPublic = PUBLIC_PATHS.includes(req.nextUrl.pathname);

  if (!session && !isPublic) {
    return NextResponse.redirect(new URL("/login", req.url));
  }

  if (session && isPublic) {
    return NextResponse.redirect(new URL("/", req.url));
  }

  return NextResponse.next();
}

// Limit middleware to these paths
export const config = {
  matcher: ["/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};

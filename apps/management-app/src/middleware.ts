import { NextRequest, NextResponse } from "next/server";
import { removeSessionCookie } from "./lib/cookies";

const PUBLIC_PATHS = ["/auth/login", "/auth/register", "/patients"];

export function middleware(req: NextRequest) {
  const session = req.cookies.get("session")?.value;
  const isPublic = PUBLIC_PATHS.includes(req.nextUrl.pathname);

  if (!session && !isPublic) {
    return NextResponse.redirect(new URL("/auth/login", req.url));
  }

  if (session && !isPublic) {
    try {
      const sessionData = JSON.parse(session);
      const now = Date.now();
      const tokenExpiry = now + (sessionData.expiresIn || 0);

      if (tokenExpiry <= now) {
        const response = NextResponse.redirect(new URL("/auth/login", req.url));
        removeSessionCookie();
        return response;
      }
    } catch (error) {
      console.error("Cookie de sessão malformado:", error);
      const response = NextResponse.redirect(new URL("/auth/login", req.url));
      removeSessionCookie();
      return response;
    }
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

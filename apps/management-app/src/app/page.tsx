"use client";

import { Button } from "@/components/ui/button";
import { logout } from "./auth/actions/actions";

export default function Home() {
  return (
    <>
      <h1>LOGGED PAGE - HELLO, WORLD!</h1>
      <Button
        onClick={async () => {
          await logout();
        }}
      >
        Log out
      </Button>
    </>
  );
}

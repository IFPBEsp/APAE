"use client";

import { Button } from "@/components/ui/button";

export default function Home() {
  return (
    <>
      <h1>LOGGED PAGE - HELLO, WORLD!</h1>
      {/* Uncomment the following lines to enable logout functionality*/}
      <Button
      // onClick={async () => {
      //   await logout();
      // }}
      >
        Log out
      </Button>
    </>
  );
}

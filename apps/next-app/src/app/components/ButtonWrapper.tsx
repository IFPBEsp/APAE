"use client";

import { Button } from "@/components/ui/button";

export default function MyButtonWrapper() {
  const handleClick = () => {
    alert("Oi");
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <Button
        onClick={handleClick}
      >
        button from Shadcn
      </Button>
    </div>
  );
}

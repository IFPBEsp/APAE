"use client";

import FloatingButton from "../components/FloatingButton";

export default function Home() {
  const handleAdicionar = () => {
    console.log("Clique no FAB!");
  };

  return (
    <div className="min-h-screen p-6">
      <h1 className="text-3xl font-bold mb-6">Floating Action Button</h1>

      <FloatingButton onClick={handleAdicionar} />
    </div>
  );
}

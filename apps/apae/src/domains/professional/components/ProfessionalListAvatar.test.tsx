import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import type React from "react";
import { ProfessionalListAvatar } from "./ProfessionalListAvatar";

vi.mock("@/components/ui/avatar", () => ({
  Avatar: ({ children }: { children: React.ReactNode }) => <span>{children}</span>,
  AvatarImage: ({ src, alt }: { src?: string; alt?: string }) => src ? <img src={src} alt={alt} /> : null,
  AvatarFallback: ({ children }: { children: React.ReactNode }) => <span>{children}</span>,
}));

describe("ProfessionalListAvatar", () => {
  it("renders photo URL when provided", () => {
    render(<ProfessionalListAvatar name="Maria" profilePhotoUrl="https://minio.example/photo" />);

    expect(screen.getByAltText("Maria")).toHaveAttribute("src", "https://minio.example/photo");
  });

  it("renders name initial when photo URL is absent", () => {
    render(<ProfessionalListAvatar name="Maria" profilePhotoUrl={null} />);

    expect(screen.getByText("M")).toBeInTheDocument();
  });
});

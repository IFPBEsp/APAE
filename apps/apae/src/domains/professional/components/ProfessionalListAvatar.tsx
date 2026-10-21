import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

interface ProfessionalListAvatarProps {
  name: string;
  profilePhotoUrl: string | null | undefined;
}

export function ProfessionalListAvatar({ name, profilePhotoUrl }: ProfessionalListAvatarProps) {
  return (
    <Avatar className="size-9">
      <AvatarImage src={profilePhotoUrl ?? undefined} alt={name} />
      <AvatarFallback className="bg-[#B2D7EC] font-semibold text-[#0D4F97]">
        {name?.charAt(0).toUpperCase() || "P"}
      </AvatarFallback>
    </Avatar>
  );
}

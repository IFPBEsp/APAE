import { Avatar, AvatarImage } from "@/components/ui/avatar";

export default function PersonDetails() {
    return (
        <div className="flex items-center justify-center h-screen">
            <Avatar>
                <AvatarImage src="https://www.inspirali.com/app/uploads/elementor/thumbs/carreira-medica-qfi1h8l88d4mqwz9hh5787b2rmfbc72p06p4ro4d8g.jpeg" />
            </Avatar>
        </div>
    );
}
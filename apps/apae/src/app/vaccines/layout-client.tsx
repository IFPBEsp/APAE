"use client";

import { useVaccinesContext } from "@/hooks/use-vaccines";
import { useEffect } from "react";
import { toast } from "react-toastify";

export function VaccinesLayoutClient({
    children,
}: {
    readonly children: React.ReactNode;
}) {
    const { feedback } = useVaccinesContext();

    useEffect(() => {
    if (feedback.error) toast.error(feedback.message);
    if (feedback.success) toast.success(feedback.message);
    }, [feedback]);

    return children;
}

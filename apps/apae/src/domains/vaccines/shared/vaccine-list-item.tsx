"use client";

import { Button } from "@/components/ui/button";
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Lock, Pencil, Trash2 } from "lucide-react";
import type { Vaccine } from "../vaccines.types";

interface VaccineListItemProps {
    vaccine: Vaccine;
    onEdit: () => void;
    onDelete: () => void;
}

export function VaccineListItem({
                                    vaccine,
                                    onEdit,
                                    onDelete,
                                }: VaccineListItemProps) {
    return (
        <div className="flex items-center justify-between gap-2 p-3 rounded-lg border bg-white hover:shadow-sm transition-shadow">
            <span className="font-medium text-gray-800">{vaccine.name}</span>
            <div className="flex items-center gap-1">
                <Button
                    variant="ghost"
                    size="icon"
                    onClick={onEdit}
                    className="h-8 w-8"
                    title="Editar"
                >
                    <Pencil className="h-4 w-4" />
                </Button>

                {vaccine.hasPatient ? (
                    <Button
                        variant="ghost"
                        size="icon"
                        disabled
                        className="h-8 w-8"
                        title="Esta vacina está vinculada a pacientes e não pode ser excluída."
                    >
                        <Lock className="h-4 w-4 text-gray-400" />
                    </Button>
                ) : (
                    <AlertDialog>
                        <AlertDialogTrigger asChild>
                            <Button
                                variant="ghost"
                                size="icon"
                                className="h-8 w-8 text-red-500 hover:text-red-600"
                                title="Excluir"
                            >
                                <Trash2 className="h-4 w-4" />
                            </Button>
                        </AlertDialogTrigger>
                        <AlertDialogContent>
                            <AlertDialogHeader>
                                <AlertDialogTitle>
                                    Excluir vacina
                                </AlertDialogTitle>
                                <AlertDialogDescription>
                                    Tem certeza que deseja excluir a vacina{" "}
                                    <strong>{vaccine.name}</strong>? Esta ação não pode ser
                                    desfeita.
                                </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                                <AlertDialogCancel>Cancelar</AlertDialogCancel>
                                <AlertDialogAction
                                    onClick={onDelete}
                                    className="bg-red-500 hover:bg-red-600 text-white"
                                >
                                    Excluir
                                </AlertDialogAction>
                            </AlertDialogFooter>
                        </AlertDialogContent>
                    </AlertDialog>
                )}
            </div>
        </div>
    );
}
import { useState } from "react";
import { justifyAbsence, uploadJustificationDocument } from "@/domains/absences/absences.api";

export function useJustifyAbsence() {
  const [justifyingAbsence, setJustifyingAbsence] = useState<{
    id: string;
    patientId: string;
  } | null>(null);
  const [justificationText, setJustificationText] = useState("");
  const [isSubmittingJustification, setIsSubmittingJustification] =
    useState(false);
  const [file, setFile] = useState<File | null>(null);

  const handleJustifyAbsence = async () => {
    if (!justifyingAbsence || !justificationText.trim()) return;
    try {
      setIsSubmittingJustification(true);
      let documentId: string | null = null;

      if (file) {
        documentId = await uploadJustificationDocument(
          justifyingAbsence.patientId,
          file,
        );
      }

      await justifyAbsence(justifyingAbsence.id, justificationText, documentId);
      window.location.reload();
    } catch (error: any) {
      console.error("Erro ao justificar falta:", error);
      alert(error.message || "Erro ao justificar falta.");
    } finally {
      setIsSubmittingJustification(false);
      setJustifyingAbsence(null);
      setJustificationText("");
      setFile(null);
    }
  };

  return {
    justifyingAbsence,
    setJustifyingAbsence,
    justificationText,
    setJustificationText,
    isSubmittingJustification,
    file,
    setFile,
    handleJustifyAbsence,
  };
}

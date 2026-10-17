import { CreateAbsenceDTO, FormDataType } from "@/types/absence";

interface BuildAbsencePayloadParams {
  generatedAppointmentId: string;
  absenceDate: string;
  data: FormDataType;
  documentId: string | null;
}

export const buildAbsencePayload = ({
  generatedAppointmentId,
  absenceDate,
  data,
  documentId,
}: BuildAbsencePayloadParams): CreateAbsenceDTO => {
  const isJustified = data.hasJustification === "yes";

  return {
    generatedAppointmentId,
    absenceDate,
    isJustified,
    justification: isJustified ? data.justificationText ?? "" : "Sem justificativa",
    justificationDocumentId: documentId,
  };
};

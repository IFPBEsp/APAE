import { format } from "date-fns";

/**
 * Filtra os slots de horário disponíveis considerando a data selecionada.
 *
 * Regras:
 * - Se a data não for hoje, todos os slots são válidos.
 * - Se a data for hoje, slots já passados são rejeitados, EXCETO:
 *   - O slot do agendamento que está sendo editado (editAppointmentHour.startsWith(slot)),
 *     que sempre é aceito para preservar a edição de agendamentos com horário passado.
 *
 * @param availableTimeSlots - Lista de slots no formato "HH:mm"
 * @param date - Data selecionada no formulário
 * @param editAppointmentHour - Horário do agendamento em edição (ex: "09:00:00"). Opcional.
 * @returns Lista de slots válidos para seleção
 */
export function getValidTimeSlots(
  availableTimeSlots: string[],
  date: Date | undefined,
  editAppointmentHour?: string
): string[] {
  const isToday =
    date && format(date, "yyyy-MM-dd") === format(new Date(), "yyyy-MM-dd");

  return availableTimeSlots.filter((slot) => {
    if (!isToday) return true;

    // Preserva o slot do agendamento em edição mesmo que já tenha passado
    if (editAppointmentHour && editAppointmentHour.startsWith(slot))
      return true;

    const [slotHour, slotMinute] = slot.split(":").map(Number);
    const now = new Date();
    const currentHour = now.getHours();
    const currentMinute = now.getMinutes();

    if (slotHour > currentHour) return true;
    if (slotHour === currentHour && slotMinute > currentMinute) return true;

    return false;
  });
}

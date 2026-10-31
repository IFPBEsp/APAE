import { format, getDay, isValid, startOfDay } from "date-fns";
import { useEffect, useRef, useState } from "react";
import {
  saveAppointment,
  updateAppointment,
} from "../appointments.api";
import type { Appointment } from '@/domains/appointments/types/appointments.types';
import { formatTimeForBackend, parseTimeFromBackend } from "../shared/formatters";
import { getValidTimeSlots } from "@/lib/appointment-utils";
import { bffFetchJson } from "@/lib/bff-client";
import { getPatients } from "@/domains/patients/patients.api";
import { getHealthProfessionals, getHealthProfessional } from "@/domains/professional/professional.api";

interface Option { value: string; label: string; }

const mapDayOfWeek: Record<number, string> = {
  1: "SEGUNDA", 2: "TERCA", 3: "QUARTA", 4: "QUINTA", 5: "SEXTA",
};

export function useAppointmentForm(editAppointment?: Appointment) {
  const isInitialMount = useRef(true);
  const prevProfessionalId = useRef(editAppointment?.professional?.id || "");
  const dataFetched = useRef(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [date, setDate] = useState<Date | undefined>(() => {
    if (editAppointment?.initialDate) {
      const parts = editAppointment.initialDate.split("-").map(Number);
      const d = new Date(parts[0], parts[1] - 1, parts[2]);
      return isValid(d) ? d : undefined;
    }
    return undefined;
  });
  const [selectedTime, setSelectedTime] = useState<string>(() =>
    editAppointment?.hour ? parseTimeFromBackend(editAppointment.hour) : ""
  );
  const [isCalendarOpen, setIsCalendarOpen] = useState(false);
  const [patient, setPatient] = useState<Option>(() => {
    const p = editAppointment?.annualRegistration?.patient;
    return p ? { value: p.id, label: p.fullName } : { value: "", label: "" };
  });
  const [professional, setProfessional] = useState<Option>(() => {
    const prof = editAppointment?.professional;
    return prof ? { value: prof.id, label: prof.name } : { value: "", label: "" };
  });
  const [listPatients, setListPatients] = useState<Option[]>([]);
  const [listaProfessional, setListaProfessionals] = useState<Option[]>([]);
  const [frequencyDays, setFrequencyDays] = useState<number>(editAppointment?.frequencyDays || 0);
  const [availabilities, setAvailabilities] = useState<{ day: string; shift: string }[]>([]);
  const [availableTimeSlots, setAvailableTimeSlots] = useState<string[]>([]);
  const [validationErrors, setValidationErrors] = useState({
    date: false, time: false, patient: false, professional: false, frequencyDays: false,
  });

  useEffect(() => {
    if (dataFetched.current) return;
    const fetchData = async () => {
      try {
        dataFetched.current = true;
        const [patients, professionals] = await Promise.all([getPatients(), getHealthProfessionals()]);
        setListPatients(patients.map((p) => ({ value: p.id, label: p.fullName })));
        setListaProfessionals(professionals.map((p) => ({ value: p.id, label: p.name })));
      } catch (error) {
        console.error("Erro ao carregar dados do formulário:", error);
        dataFetched.current = false;
      }
    };
    fetchData();
  }, []);

  useEffect(() => {
    const loadProfessionalData = async () => {
      if (professional.value) {
        try {
          const data = await getHealthProfessional(professional.value);
          setAvailabilities(data.availabilities || []);
        } catch { setAvailabilities([]); }
      } else { setAvailabilities([]); }
    };
    loadProfessionalData();
    if (!isInitialMount.current && professional.value !== prevProfessionalId.current) {
      setSelectedTime("");
    }
    prevProfessionalId.current = professional.value;
    isInitialMount.current = false;
  }, [professional.value]);

  useEffect(() => {
    const fetchAvailableTimes = async () => {
      if (!date || !professional.value) return;
      try {
        const formattedDate = format(date, "yyyy-MM-dd");
        const data = await bffFetchJson<string[]>(
          `/apae-geral/api/professionals/${professional.value}/available-times?date=${formattedDate}`,
        );
        setAvailableTimeSlots(data || []);
      } catch { setAvailableTimeSlots([]); }
    };
    fetchAvailableTimes();
  }, [date, professional.value]);

  const isDayDisabled = (day: Date) => {
    const today = startOfDay(new Date());
    const dayOfWeek = getDay(day);
    if (dayOfWeek === 0 || dayOfWeek === 6) return true;
    const dayName = mapDayOfWeek[dayOfWeek];
    const isPast = day.getTime() < today.getTime();
    const professionalWorksThisDay = availabilities.some((a) => a.day === dayName);
    if (editAppointment?.initialDate) {
      const [y, m, d] = editAppointment.initialDate.split("-").map(Number);
      const editDate = new Date(y, m - 1, d);
      if (format(day, "yyyy-MM-dd") === format(editDate, "yyyy-MM-dd")) return false;
    }
    return isPast || !professionalWorksThisDay;
  };

  const validTimeSlots = getValidTimeSlots(
    availableTimeSlots,
    date,
    editAppointment?.hour,
  );

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (isSubmitting == true) {
      return;
    }
    setSubmitError(null);
    const errors = {
      date: !date || !isValid(date), time: !selectedTime,
      patient: !patient.value, professional: !professional.value,
      frequencyDays: !frequencyDays || isNaN(frequencyDays) || frequencyDays <= 0,
    };
    setValidationErrors(errors);
    if (Object.values(errors).some(Boolean)) return;
    try {
      setIsSubmitting(true);
      const initialDate = format(date!, "yyyy-MM-dd");
      const hourStr = formatTimeForBackend(selectedTime);
      if (editAppointment?.id) {
        await updateAppointment(editAppointment.id, {
          professionalId: professional.value,
          annualRegistrationId: editAppointment.annualRegistration.id,
          initialDate, hour: hourStr, frequencyDays,
        });
      } else {
        await saveAppointment({
          patientId: patient.value, professionalId: professional.value,
          initialDate, hour: hourStr, frequencyDays,
        });
      }
      window.location.reload();
    } catch (error: any) {
      setSubmitError(error.message || "Erro inesperado ao salvar o agendamento.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return {
    date, setDate, selectedTime, setSelectedTime,
    isCalendarOpen, setIsCalendarOpen,
    patient, setPatient, professional, setProfessional,
    listPatients, listaProfessional, frequencyDays, setFrequencyDays,
    validTimeSlots, validationErrors, submitError, isSubmitting,
    isDayDisabled, handleSubmit,
  };
}

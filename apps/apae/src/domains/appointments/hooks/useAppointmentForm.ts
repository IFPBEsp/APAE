import { format, getDay, isValid, startOfDay } from "date-fns";
import { useEffect, useRef, useState } from "react";
import {
  getPatients,
  getHealthProfessionals,
  saveAppointment,
  updateAppointment,
} from "../appointments.api";
import type { Appointment } from '@/domains/appointments/types/appointments.types';

interface Option { value: string; label: string; }

const mapDayOfWeek: Record<number, string> = {
  1: "SEGUNDA", 2: "TERCA", 3: "QUARTA", 4: "QUINTA", 5: "SEXTA",
};

export function useAppointmentForm(editAppointment?: Appointment) {
  const isInitialMount = useRef(true);
  const prevProfessionalId = useRef(editAppointment?.professional?.id || "");
  const dataFetched = useRef(false);
  const [submitError, setSubmitError] = useState<string | null>(null);
  const [date, setDate] = useState<Date | undefined>(() => {
    if (editAppointment?.initialDate) {
      const parts = editAppointment.initialDate.split("-").map(Number);
      const d = new Date(parts[0], parts[1] - 1, parts[2]);
      return isValid(d) ? d : undefined;
    }
    return undefined;
  });
  const [selectedTime, setSelectedTime] = useState<string>(() =>
    editAppointment?.hour ? editAppointment.hour.slice(0, 5) : ""
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
          const res = await fetch(`/apae-geral/api/professionals/${professional.value}`);
          const data = await res.json();
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
        const res = await fetch(`/apae-geral/api/professionals/${professional.value}/available-times?date=${formattedDate}`);
        const data = await res.json();
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

  const isToday = date && format(date, "yyyy-MM-dd") === format(new Date(), "yyyy-MM-dd");
  const validTimeSlots = availableTimeSlots.filter((slot) => {
    if (!isToday) return true;
    if (editAppointment && editAppointment.hour.startsWith(slot)) return true;
    const [slotHour, slotMinute] = slot.split(":").map(Number);
    const now = new Date();
    if (slotHour > now.getHours()) return true;
    if (slotHour === now.getHours() && slotMinute > now.getMinutes()) return true;
    return false;
  });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSubmitError(null);
    const errors = {
      date: !date || !isValid(date), time: !selectedTime,
      patient: !patient.value, professional: !professional.value,
      frequencyDays: !frequencyDays || isNaN(frequencyDays) || frequencyDays <= 0,
    };
    setValidationErrors(errors);
    if (Object.values(errors).some(Boolean)) return;
    try {
      const initialDate = format(date!, "yyyy-MM-dd");
      const hourStr = `${selectedTime}:00`;
      if (editAppointment?.id) {
        await updateAppointment(editAppointment.id, {
          professionalId: professional.value,
          annualRegistrationId: editAppointment.annualRegistration.id,
          serviceId: editAppointment.serviceId,
          initialDate, hour: hourStr, frequencyDays,
        });
      } else {
        await saveAppointment({
          patientId: patient.value, professionalId: professional.value,
          serviceId: "ea4c3a4d-c3f4-4a83-ab29-ff24c50e844c",
          initialDate, hour: hourStr, frequencyDays,
        });
      }
      window.location.reload();
    } catch (error: any) {
      setSubmitError(error.message || "Erro inesperado ao salvar o agendamento.");
    }
  };

  return {
    date, setDate, selectedTime, setSelectedTime,
    isCalendarOpen, setIsCalendarOpen,
    patient, setPatient, professional, setProfessional,
    listPatients, listaProfessional, frequencyDays, setFrequencyDays,
    validTimeSlots, validationErrors, submitError,
    isDayDisabled, handleSubmit,
  };
}
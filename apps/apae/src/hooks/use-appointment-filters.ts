"use client";

import { useState } from "react";

import { Appointment } from "@/app/services/appointmentService";
import { formatDatePTBR } from "@/lib/utils";

interface AppointmentFiltersResult {
  selectedDate: Date | undefined;
  setSelectedDate: (d: Date | undefined) => void;
  selectedArea: string;
  setSelectedArea: (a: string) => void;
  selectedStatus: string;
  setSelectedStatus: (s: string) => void;
  searchName: string;
  setSearchName: (n: string) => void;
  filteredAppointments: Appointment[];
  clearFilter: () => void;
}

export function useAppointmentFilters(
  appointments: Appointment[]
): AppointmentFiltersResult {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(
    undefined
  );
  const [selectedArea, setSelectedArea] = useState("");
  const [selectedStatus, setSelectedStatus] = useState("");
  const [searchName, setSearchName] = useState("");

  const filteredAppointments = appointments.filter((appointment) => {
    const matchesDate = selectedDate
      ? formatDatePTBR(appointment.initialDate) ===
        formatDatePTBR(selectedDate.toString())
      : true;

    const search = searchName.toLowerCase();

    const matchesSearch =
      appointment.annualRegistration.patient.fullName
        .toLowerCase()
        .includes(search) ||
      appointment.professional.name.toLowerCase().includes(search);

    const matchesArea = selectedArea
      ? appointment.professional.healthSector === selectedArea
      : true;

    const matchesStatus = selectedStatus
      ? selectedStatus === "ativo"
        ? appointment.isActive === true
        : appointment.isActive === false
      : true;

    return matchesDate && matchesSearch && matchesArea && matchesStatus;
  });

  const clearFilter = () => {
    setSelectedArea("");
    setSearchName("");
    setSelectedDate(undefined);
    setSelectedStatus("");
  };

  return {
    selectedDate,
    setSelectedDate,
    selectedArea,
    setSelectedArea,
    selectedStatus,
    setSelectedStatus,
    searchName,
    setSearchName,
    filteredAppointments,
    clearFilter,
  };
}

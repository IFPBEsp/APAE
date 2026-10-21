"use client";

import { useState } from "react";
import { AbsenceSearchInput } from "@/domains/absences/components/AbsenceSearchInput";
import { AbsenceStatsCards } from "@/domains/absences/components/AbsenceStatsCards";
import { JustifyAbsenceDialog } from "@/domains/absences/components/JustifyAbsenceDialog";
import { PatientsAbsenceTable } from "@/domains/absences/components/PatientsAbsenceTable";
import { useAbsencesList } from "@/domains/absences/hooks/useAbsencesList";
import { useJustifyAbsence } from "@/domains/absences/hooks/useJustifyAbsence";

export default function AbsenceDetails() {
  const {
    searchName,
    setSearchName,
    patientsWithAbsences,
    loading,
    pagination,
    fetchData,
    statistics,
    handleDownload,
  } = useAbsencesList();

  const {
    justifyingAbsence,
    setJustifyingAbsence,
    justificationText,
    setJustificationText,
    isSubmittingJustification,
    file,
    setFile,
    handleJustifyAbsence,
  } = useJustifyAbsence();

  const [expandedPatient, setExpandedPatient] = useState<string | null>(null);

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 w-full max-w-none">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Controle de Faltas
          </h1>
        </div>

        <AbsenceStatsCards statistics={statistics} />

        <AbsenceSearchInput
          searchName={searchName}
          onSearchChange={setSearchName}
        />

        <PatientsAbsenceTable
          patientsWithAbsences={patientsWithAbsences}
          loading={loading}
          pagination={pagination}
          expandedPatient={expandedPatient}
          onToggleExpand={(patientId) =>
            setExpandedPatient(expandedPatient === patientId ? null : patientId)
          }
          onPageChange={(page) => fetchData(page, searchName)}
          onJustify={setJustifyingAbsence}
          onDownload={handleDownload}
        />

        <JustifyAbsenceDialog
          isOpen={Boolean(justifyingAbsence)}
          justificationText={justificationText}
          onJustificationTextChange={setJustificationText}
          file={file}
          onFileChange={setFile}
          isSubmitting={isSubmittingJustification}
          onCancel={() => {
            setJustifyingAbsence(null);
            setJustificationText("");
            setFile(null);
          }}
          onSubmit={handleJustifyAbsence}
        />
      </main>
    </div>
  );
}

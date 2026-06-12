import { useCallback, useMemo, useState } from "react";
import { getProfessionalDocuments, removeProfessionalDocument } from "@/services/profissional-service";
import { DocumentWithUrl } from "@/types/document";
import { isValidFile } from "@/domains/professional/shared/professional.utils";

export function useProfessionalDocs(professionalId: string | undefined) {
  const [docs, setDocs] = useState<DocumentWithUrl[]>([]);
  const [docsLoading, setDocsLoading] = useState(false);
  const [docsError, setDocsError] = useState<string | null>(null);

  const [curriculumFile, setCurriculumFile] = useState<File | null>(null);
  const [volunteerFile, setVolunteerFile] = useState<File | null>(null);
  const [attachmentFiles, setAttachmentFiles] = useState<File[]>([]);

  const [removingIds, setRemovingIds] = useState<Set<string>>(new Set());
  const [removeModalOpen, setRemoveModalOpen] = useState(false);
  const [docToRemove, setDocToRemove] = useState<DocumentWithUrl | null>(null);

  const hasAnyUpload = !!(curriculumFile || volunteerFile || attachmentFiles.length > 0);

  const refreshDocuments = useCallback(async (id: string) => {
    setDocsLoading(true);
    setDocsError(null);
    try {
      const data = await getProfessionalDocuments(id);
      setDocs(data);
    } catch (e: unknown) {
      setDocsError((e as Error)?.message ?? "Erro ao carregar documentos");
    } finally {
      setDocsLoading(false);
    }
  }, []);

  const groupedDocs = useMemo(() => ({
    curriculum: docs.find((d) => d.type === "CURRICULUM"),
    volunteer: docs.find((d) => d.type === "VOLUNTEER_AGREEMENT"),
    attachments: docs.filter((d) => d.type === "ATTACHMENTANY"),
  }), [docs]);

  function openRemoveModal(doc: DocumentWithUrl) {
    setDocToRemove(doc);
    setRemoveModalOpen(true);
  }

  async function confirmRemove() {
    if (!professionalId || !docToRemove) return;
    const idStr = String(docToRemove.id);
    setRemovingIds((prev) => new Set(prev).add(idStr));
    try {
      await removeProfessionalDocument(professionalId, docToRemove.id);
      await refreshDocuments(professionalId);
      setRemoveModalOpen(false);
      setDocToRemove(null);
    } catch (e: unknown) {
      alert((e as Error)?.message ?? "Erro ao remover documento");
    } finally {
      setRemovingIds((prev) => { const s = new Set(prev); s.delete(idStr); return s; });
    }
  }

  function buildFormData() {
    const fd = new FormData();
    if (volunteerFile) fd.append("volunteerAgreement", volunteerFile);
    if (curriculumFile) fd.append("curriculum", curriculumFile);
    for (const f of attachmentFiles) fd.append("attachmentAny", f);
    return fd;
  }

  function clearFiles() {
    setCurriculumFile(null);
    setVolunteerFile(null);
    setAttachmentFiles([]);
  }

  const isConfirmBusy = !!docToRemove && removingIds.has(String(docToRemove.id));

  return {
    docs, docsLoading, docsError,
    groupedDocs,
    curriculumFile, setCurriculumFile,
    volunteerFile, setVolunteerFile,
    attachmentFiles, setAttachmentFiles,
    hasAnyUpload,
    removingIds,
    removeModalOpen, setRemoveModalOpen,
    docToRemove,
    isConfirmBusy,
    openRemoveModal,
    confirmRemove,
    refreshDocuments,
    buildFormData,
    clearFiles,
    isValidFile,
  };
}
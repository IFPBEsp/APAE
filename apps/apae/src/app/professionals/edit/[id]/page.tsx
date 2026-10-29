"use client";

import { useEffect, useMemo, JSX } from "react";
import { useRouter } from "next/navigation";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/components/ui/button";
import { Form } from "@/components/ui/form";
import { useGetByIdProfessional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfessional } from "@/hooks/profissional/use-update-profissional";
import { useUpdateProfessionalDocuments } from "@/hooks/profissional/use-update-professional-documents";
import { updateProfessionalSchema, UpdateProfessionalFormValues } from "@/schemas/profissional.schema";
import { ProfessionalFormFields } from "@/domains/professional/components/ProfessionalFormFields";
import { ProfessionalDocuments } from "@/domains/professional/components/ProfessionalDocuments";
import { usePhotoUpload } from "@/hooks/use-photo-upload";
import { PhotoUpload } from "@/components/shared/PhotoUpload";
import { useProfessionalDocs } from "@/hooks/profissional/use-professional-docs";
import {
  mapProfessionalToForm,
  buildProfessionalPayload,
} from "@/domains/professional/shared/professional.utils";


export default function ProfessionalUpdate(): JSX.Element {
  const router = useRouter();
  const { professional, loading: loadingProf, error: errorProf } = useGetByIdProfessional();
  const { updateProfessional, loading, error, success } = useUpdateProfessional();
  const { upload, loadingDocs, errorDocs, successDocs } = useUpdateProfessionalDocuments();
  const {
    fileInputRef,
    selectedPhoto,
    selectPhoto,
    photoPreviewUrl,
    photoError,
    photoSuccess,
    uploadPhoto,
  } = usePhotoUpload({ buildUrl: (id) => `/apae-geral/api/professionals/${id}/photo`, method: "PATCH" });
  const {
    docs: docsList,
    docsLoading,
    docsError,
    curriculumFile,
    setCurriculumFile,
    volunteerFile,
    setVolunteerFile,
    attachmentFiles,
    setAttachmentFiles,
    hasAnyUpload,
    removingIds,
    removeModalOpen,
    setRemoveModalOpen,
    docToRemove,
    isConfirmBusy,
    openRemoveModal,
    confirmRemove,
    refreshDocuments,
    buildFormData,
    clearFiles,
    isValidFile,
  } = useProfessionalDocs(professional?.id);

  const form = useForm<UpdateProfessionalFormValues>({
    resolver: zodResolver(updateProfessionalSchema),
    defaultValues: {
      fullName: "", email: "", cpf: "", professionalDocument: "", serviceArea: "",
      phone: "", rg: "", state: "", city: "", neighborhood: "",
      street: "", number: "", complement: "", cep: "", availability: [],
    },
  });

  useEffect(() => {
    if (!professional) return;
    form.reset(mapProfessionalToForm(professional));
  }, [professional, form]);

  useEffect(() => {
    if (!professional?.id) return;
    refreshDocuments(professional.id);
  }, [professional?.id, refreshDocuments]);

  const groupedDocs = useMemo(() => {
    const curriculum = docsList.find((d) => d.type === "CURRICULUM");
    const volunteer = docsList.find((d) => d.type === "VOLUNTEER_AGREEMENT");
    const attachments = docsList.filter((d) => d.type === "ATTACHMENTANY");
    const photoDoc = docsList.find((d) => d.type === "PHOTO");
    return { curriculum, volunteer, attachments, photo: photoDoc };
  }, [docsList]);

  const onSubmit: SubmitHandler<UpdateProfessionalFormValues> = async (values) => {
    if (!professional?.id) return;

    const ok = await updateProfessional(professional.id, buildProfessionalPayload(values));
    if (!ok) return;

    if (hasAnyUpload) {
      await upload(professional.id, buildFormData());
      clearFiles();
      await refreshDocuments(professional.id);
    }

    if (selectedPhoto) {
      const okPhoto = await uploadPhoto(professional.id);
      if (!okPhoto) return;
    }

    router.push("/professionals");
  };

  if (loadingProf) return <p>Carregando dados...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  return (
    <div className="p-0">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 w-full max-w-2xl">
          {/* Campos comuns (fullName ... complement) + disponibilidade */}
          <ProfessionalFormFields />

          <PhotoUpload
            fileInputRef={fileInputRef}
            selectedPhoto={selectedPhoto}
            photoPreviewUrl={photoPreviewUrl}
            profilePhotoUrl={groupedDocs.photo?.url || professional?.profilePhotoUrl || professional?.profilePhoto || undefined}
            photoError={photoError}
            photoSuccess={photoSuccess}
            selectPhoto={selectPhoto}
          />

          <ProfessionalDocuments
            groupedDocs={groupedDocs}
            docsLoading={docsLoading}
            docsError={docsError}
            removingIds={removingIds}
            removeModalOpen={removeModalOpen}
            setRemoveModalOpen={setRemoveModalOpen}
            docToRemove={docToRemove}
            isConfirmBusy={isConfirmBusy}
            openRemoveModal={openRemoveModal}
            confirmRemove={confirmRemove}
            curriculumFile={curriculumFile}
            volunteerFile={volunteerFile}
            attachmentFiles={attachmentFiles}
            setCurriculumFile={setCurriculumFile}
            setVolunteerFile={setVolunteerFile}
            setAttachmentFiles={setAttachmentFiles}
            isValidFile={isValidFile}
            errorDocs={errorDocs}
            successDocs={successDocs}
            hasAnyUpload={hasAnyUpload}
            loadingDocs={loadingDocs}
          />

          {(loading || loadingDocs) && (
            <p className="text-blue-500">{loading ? "Salvando perfil..." : "Enviando documentos..."}</p>
          )}
          {error && <p className="text-red-500">{error}</p>}
          {success && <p className="text-green-600">Profissional atualizado com sucesso!</p>}

          <div className="flex justify-end gap-4">
            <Button type="button" variant="outline" onClick={() => router.push("/professionals")}>Cancelar</Button>
            <Button type="submit" className="bg-[#0D4F97] hover:bg-blue-900" disabled={form.formState.isSubmitting || loading || loadingDocs}>Salvar</Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
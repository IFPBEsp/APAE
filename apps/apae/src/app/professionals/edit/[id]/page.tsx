"use client";

import { useEffect, useMemo, JSX } from "react";
import { useRouter } from "next/navigation";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useGetByIdProfessional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfessional } from "@/hooks/profissional/use-update-profissional";
import { useUpdateProfessionalDocuments } from "@/hooks/profissional/use-update-professional-documents";
import { updateProfessionalSchema, UpdateProfessionalFormValues } from "@/schemas/profissional.schema";
import { ProfessionalFormFields } from "@/domains/professional/components/ProfessionalFormFields";
import { ProfessionalDocuments } from "@/domains/professional/components/ProfessionalDocuments";
import { useProfessionalPhoto } from "@/hooks/profissional/use-professional-photo";
import { useProfessionalDocs } from "@/hooks/profissional/use-professional-docs";
import {
  mapProfessionalToForm,
  buildProfessionalPayload,
} from "@/domains/professional/shared/professional.utils";
import { MAX_FILE_SIZE_BYTES, MAX_FILE_SIZE_LABEL } from "@/lib/constants";

export default function ProfessionalUpdate(): JSX.Element {
  const router = useRouter();
  const { professional, loading: loadingProf, error: errorProf } = useGetByIdProfessional();
  const { updateProfessional, loading, error, success } = useUpdateProfessional();
  const { upload, loadingDocs, errorDocs, successDocs } = useUpdateProfessionalDocuments();
  const {
    fileInputRef,
    selectedPhoto,
    setSelectedPhoto,
    photoPreviewUrl,
    photoError,
    uploadPhoto,
  } = useProfessionalPhoto();
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

          {/* A partir daqui é específico da edição */}
          <FormField
            control={form.control}
            name="photo"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-medium">
                  Selecione uma foto*
                </FormLabel>
                <FormControl>
                  <div className="flex flex-col items-start gap-4 w-full">
                    <input
                      ref={fileInputRef}
                      type="file"
                      id={`${field.name}-upload`}
                      className="hidden"
                      accept="image/png,image/jpeg,image/jpg,image/webp"
                      onChange={(e) => {
                        const file = e.target.files?.[0];
                        if (!file) {
                          field.onChange(null);
                          setSelectedPhoto(null);
                          return;
                        }
                        const allowedTypes = [
                          "image/png",
                          "image/jpeg",
                          "image/jpg",
                          "image/webp",
                        ];
                        const maxSize = MAX_FILE_SIZE_BYTES;
                        if (
                          !allowedTypes.includes(file.type) ||
                          file.size <= 0 ||
                          file.size > maxSize
                        ) {
                          alert(
                            `Apenas imagens PNG, JPG ou WEBP até ${MAX_FILE_SIZE_LABEL} são permitidas`,
                          );
                          if (fileInputRef.current) {
                            fileInputRef.current.value = "";
                          }
                          field.onChange(null);
                          setSelectedPhoto(null);
                          return;
                        }
                        field.onChange(file);
                        setSelectedPhoto(file);
                      }}
                    />
                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="relative group mr-auto rounded-full transition-transform hover:scale-105"
                    >
                      <div className="w-32 h-32 overflow-hidden rounded-full border-2 border-dashed border-gray-300 bg-gray-50 flex items-center justify-center">
                        {photoPreviewUrl || groupedDocs.photo?.url || professional?.profilePhotoUrl || professional?.profilePhoto ? (
                          // eslint-disable-next-line @next/next/no-img-element
                          <img
                            src={photoPreviewUrl || groupedDocs.photo?.url || professional?.profilePhotoUrl || professional?.profilePhoto || undefined}
                            alt="Foto do profissional"
                            className="h-full w-full object-cover"
                          />
                        ) : (
                          <span className="text-xs text-gray-500">Sem foto</span>
                        )}
                      </div>
                      <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity bg-black/20 rounded-full">
                        <span className="bg-white text-black text-[10px] font-bold px-2 py-1 rounded shadow-sm cursor-pointer">
                          Escolher foto
                        </span>
                      </div>
                    </button>
                    <p className="text-xs text-gray-500">
                      PNG, JPG ou WEBP até {MAX_FILE_SIZE_LABEL}
                    </p>
                    {field.value && (
                      <div className="flex items-center gap-2">
                        <p className="text-xs text-gray-600">
                          Selecionado: {field.value.name}
                        </p>
                        <Button
                          type="button"
                          variant="outline"
                          size="sm"
                          onClick={() => {
                            field.onChange(null);
                            setSelectedPhoto(null);
                            if (fileInputRef.current) {
                              fileInputRef.current.value = "";
                            }
                          }}
                        >
                          Remover
                        </Button>
                      </div>
                    )}
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
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
          {photoError && <p className="text-red-500">{photoError}</p>}
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
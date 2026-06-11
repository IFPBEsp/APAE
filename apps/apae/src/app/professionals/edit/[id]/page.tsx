"use client";

import { JSX } from "react";
import { useRouter } from "next/navigation";
import { Controller, type SubmitHandler } from "react-hook-form";
import * as z from "zod";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { InputMask } from "@react-input/mask";

import { useGetByIdProfessional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfessional } from "@/hooks/profissional/use-update-profissional";
import { useUpdateProfessionalDocuments } from "@/hooks/profissional/use-update-professional-documents";
import { updateProfessionalSchema } from "@/schemas/profissional.schema";
import { STATES } from "@/lib/states";

import HealthAreaSelect from "@/components/shared/HealthAreaSelect";
import Availability from "@/components/forms/AvailabilityForm";

import { ProfessionalPhoto } from "@/domains/professional/components/ProfessionalPhoto";
import { ProfessionalDocuments } from "@/domains/professional/components/ProfessionalDocuments";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect } from "react";
import { useProfessionalPhoto } from "@/hooks/profissional/use-professional-photo";
import { useProfessionalDocs } from "@/hooks/profissional/use-professional-docs";
import { mapProfessionalToForm, buildUpdatePayload } from "@/domains/professional/shared/professional.utils";

type UpdateFormValues = z.infer<typeof updateProfessionalSchema>;

export default function ProfessionalUpdate(): JSX.Element {
  const router = useRouter();

  const { professional, loading: loadingProf, error: errorProf } = useGetByIdProfessional();
  const { updateProfessional, loading, error, success } = useUpdateProfessional();
  const { upload, loadingDocs, errorDocs, successDocs } = useUpdateProfessionalDocuments();

  const photo = useProfessionalPhoto();
  const docs = useProfessionalDocs(professional?.id);

  const form = useForm<UpdateFormValues>({
    resolver: zodResolver(updateProfessionalSchema),
    defaultValues: {
      fullName: "", email: "", professionalDocument: "", serviceArea: "",
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
    const curriculum = docs.find((d) => d.type === "CURRICULUM");
    const volunteer = docs.find((d) => d.type === "VOLUNTEER_AGREEMENT");
    const attachments = docs.filter((d) => d.type === "ATTACHMENTANY");
    const photo = docs.find((d) => d.type === "PHOTO");
    return { curriculum, volunteer, attachments, photo };
  }, [docs]);

  function openRemoveModal(doc: DocumentWithUrl) {
    setDocToRemove(doc);
    setRemoveModalOpen(true);
  }

  async function confirmRemove() {
    if (!professional?.id || !docToRemove) return;

    const idStr = String(docToRemove.id);

    setRemovingIds((prev) => {
      const next = new Set(prev);
      next.add(idStr);
      return next;
    });

    try {
      await removeProfessionalDocument(professional.id, docToRemove.id);
      await refreshDocuments(professional.id);
      setRemoveModalOpen(false);
      setDocToRemove(null);
    } catch (e: unknown) {
      console.error(e);
      alert((e as Error)?.message ?? "Erro ao remover documento");
    } finally {
      setRemovingIds((prev) => {
        const next = new Set(prev);
        next.delete(idStr);
        return next;
      });
    }
  }

  const onSubmit: SubmitHandler<UpdateFormValues> = async (values) => {
    if (!professional?.id) return;

    const availabilities = values.availability
      .filter((d) => d?.checked)
      .map((d) => ({ day: d?.day, shift: d?.shift }));

    const ok = await updateProfessional(professional.id, buildUpdatePayload(values, availabilities));
    if (!ok) return;

    if (docs.hasAnyUpload) {
      await upload(professional.id, docs.buildFormData());
      docs.clearFiles();
      await docs.refreshDocuments(professional.id);
    }

    if (selectedPhoto) {
      setPhotoError(null);
      setPhotoSuccess(false);
      try {
        const photoData = new FormData();
        photoData.append("file", selectedPhoto);

        const response = await fetch(`/apae-geral/api/professionals/${professional.id}/photo`, {
          method: "PATCH",
          body: photoData,
        });

        if (!response.ok) {
          const body = await response.json().catch(() => ({}));
          throw new Error(body?.message ?? "Erro ao enviar foto");
        }

        setPhotoSuccess(true);
        setSelectedPhoto(null);
      } catch (e: unknown) {
        setPhotoError((e as Error)?.message ?? "Erro ao enviar foto");
        return;
      }
    }

    router.push("/professionals");
  };

  if (loadingProf) return <p>Carregando dados...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  return (
    <div className="p-0">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6 w-full max-w-2xl">

          <FormField control={form.control} name="fullName" render={({ field }) => (
            <FormItem><FormLabel>Nome completo *</FormLabel><FormControl><Input placeholder="Ex: Maria da Silva" {...field} /></FormControl><FormMessage /></FormItem>
          )} />

          <FormField control={form.control} name="email" render={({ field }) => (
            <FormItem><FormLabel>Email *</FormLabel><FormControl><Input type="email" placeholder="profissional@exemplo.com" {...field} /></FormControl><FormMessage /></FormItem>
          )} />

          <div className="grid grid-cols-2 gap-4">
            <FormField control={form.control} name="professionalDocument" render={({ field }) => (
              <FormItem><FormLabel>Documento profissional</FormLabel><FormControl><Input placeholder="Ex: CRM/SP 123456" {...field} value={field.value || ""} /></FormControl><FormMessage /></FormItem>
            )} />
            <Controller control={form.control} name="serviceArea" render={({ field, fieldState }) => (
              <FormItem><FormLabel>Área de atendimento *</FormLabel><FormControl><HealthAreaSelect value={field.value} onChange={field.onChange} /></FormControl><FormMessage>{fieldState.error?.message}</FormMessage></FormItem>
            )} />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField control={form.control} name="rg" render={({ field }) => (
              <FormItem><FormLabel>RG *</FormLabel><FormControl><Input placeholder="Ex: 1234567" {...field} /></FormControl><FormMessage /></FormItem>
            )} />
            <Controller control={form.control} name="phone" render={({ field, fieldState }) => (
              <FormItem><FormLabel>Telefone *</FormLabel><FormControl>
                <InputMask mask="(__) _____-____" replacement={{ _: /\d/ }} value={field.value ?? ""} onChange={(e) => field.onChange(e.target.value)} onBlur={field.onBlur} placeholder="(xx) xxxxx-xxxx" className="w-full rounded-md border px-3 py-2" />
              </FormControl><FormMessage>{fieldState.error?.message}</FormMessage></FormItem>
            )} />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Controller control={form.control} name="state" render={({ field, fieldState }) => (
              <FormItem><FormLabel>Estado *</FormLabel><FormControl>
                <Select onValueChange={field.onChange} value={field.value}>
                  <SelectTrigger className={`w-full ${fieldState.invalid ? "border-red-500" : "border-gray-300"}`}><SelectValue placeholder="Selecione um estado" /></SelectTrigger>
                  <SelectContent>{STATES.map((s) => <SelectItem key={s} value={s}>{s}</SelectItem>)}</SelectContent>
                </Select>
              </FormControl><FormMessage>{fieldState.error?.message}</FormMessage></FormItem>
            )} />
            <FormField control={form.control} name="city" render={({ field }) => (
              <FormItem><FormLabel>Cidade *</FormLabel><FormControl><Input placeholder="Ex: João Pessoa" {...field} /></FormControl><FormMessage /></FormItem>
            )} />
          </div>

          <FormField control={form.control} name="street" render={({ field }) => (
            <FormItem><FormLabel>Endereço *</FormLabel><FormControl><Input placeholder="Ex: Rua das Flores" {...field} /></FormControl><FormMessage /></FormItem>
          )} />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField control={form.control} name="neighborhood" render={({ field }) => (
              <FormItem><FormLabel>Bairro *</FormLabel><FormControl><Input placeholder="Ex: Centro" {...field} /></FormControl><FormMessage /></FormItem>
            )} />
            <Controller control={form.control} name="cep" render={({ field, fieldState }) => (
              <FormItem><FormLabel>CEP *</FormLabel><FormControl>
                <InputMask mask="_____-___" replacement={{ _: /\d/ }} value={field.value ?? ""} onChange={(e) => field.onChange(e.target.value)} onBlur={field.onBlur} placeholder="12345-678" className="w-full rounded-md border px-3 py-2" />
              </FormControl><FormMessage>{fieldState.error?.message}</FormMessage></FormItem>
            )} />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField control={form.control} name="number" render={({ field }) => (
              <FormItem><FormLabel>Número *</FormLabel><FormControl><Input placeholder="Ex: 123" {...field} /></FormControl><FormMessage /></FormItem>
            )} />
            <FormField control={form.control} name="complement" render={({ field }) => (
              <FormItem><FormLabel>Complemento</FormLabel><FormControl><Input placeholder="Ex: Apt 101" {...field} /></FormControl><FormMessage /></FormItem>
            )} />
          </div>

          <Availability control={form.control} watch={form.watch} />

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

                        const maxSize = 5 * 1024 * 1024;

                        if (
                          !allowedTypes.includes(file.type) ||
                          file.size <= 0 ||
                          file.size > maxSize
                        ) {
                          alert(
                            "Apenas imagens PNG, JPG ou WEBP até 5MB são permitidas",
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
                      <Avatar className="w-32 h-32 border-2 border-dashed border-gray-300 bg-gray-50 cursor-pointer flex items-center justify-center">
                        <AvatarImage
                          src={photoPreviewUrl || groupedDocs.photo?.url || undefined}
                          alt="Foto do profissional"
                        />

                        <AvatarFallback className="bg-transparent">
                          <User className="w-12 h-12 text-gray-400" />
                        </AvatarFallback>
                      </Avatar>

                      <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity bg-black/20 rounded-full">
                        <span className="bg-white text-black text-[10px] font-bold px-2 py-1 rounded shadow-sm cursor-pointer">
                          Escolher foto
                        </span>
                      </div>
                    </button>

                    <p className="text-xs text-gray-500">
                      PNG, JPG ou WEBP até 5MB
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
            groupedDocs={docs.groupedDocs}
            docsLoading={docs.docsLoading}
            docsError={docs.docsError}
            removingIds={docs.removingIds}
            removeModalOpen={docs.removeModalOpen}
            setRemoveModalOpen={docs.setRemoveModalOpen}
            docToRemove={docs.docToRemove}
            isConfirmBusy={docs.isConfirmBusy}
            openRemoveModal={docs.openRemoveModal}
            confirmRemove={docs.confirmRemove}
            curriculumFile={docs.curriculumFile}
            volunteerFile={docs.volunteerFile}
            attachmentFiles={docs.attachmentFiles}
            setCurriculumFile={docs.setCurriculumFile}
            setVolunteerFile={docs.setVolunteerFile}
            setAttachmentFiles={docs.setAttachmentFiles}
            isValidFile={docs.isValidFile}
            errorDocs={errorDocs}
            successDocs={successDocs}
            hasAnyUpload={docs.hasAnyUpload}
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
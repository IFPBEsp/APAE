"use client";

import { JSX, useEffect, useMemo, useState } from "react";
import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { InputMask } from "@react-input/mask";
import { useRouter } from "next/navigation";
import { Trash2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";

import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

import { useGetByIdProfissional } from "@/hooks/profissional/use-get-by-id-profissional";
import { useUpdateProfissional } from "@/hooks/profissional/use-update-profissional";
import { useUpdateProfessionalDocuments } from "@/hooks/profissional/use-update-professional-documents";

import { updateProfessionalSchema } from "@/schemas/profissional.schema";
import { STATES } from "@/lib/states";

import HealthAreaSelect from "@/components/shared/HealthAreaSelect";
import Disponibilidade from "@/components/forms/DisponibilidadeForm";
import { gerarMatrizDisponibilidade } from "@/utils/disponibilidade.utils";

import {
  getProfessionalDocuments,
  removeProfessionalDocument,
} from "@/services/profissional-service";
import { DocumentWithUrl } from "@/types/document";

type UpdateFormValues = z.infer<typeof updateProfessionalSchema>;

export default function AtualizarProfissional(): JSX.Element {
  const router = useRouter();

  const {
    profissional,
    loading: loadingProf,
    error: errorProf,
  } = useGetByIdProfissional();

  const { updateProfissional, loading, error, success } =
    useUpdateProfissional();

  const { upload, loadingDocs, errorDocs, successDocs } =
    useUpdateProfessionalDocuments();

  const [docs, setDocs] = useState<DocumentWithUrl[]>([]);
  const [docsLoading, setDocsLoading] = useState(false);
  const [docsError, setDocsError] = useState<string | null>(null);

  const [curriculumFile, setCurriculumFile] = useState<File | null>(null);
  const [volunteerFile, setVolunteerFile] = useState<File | null>(null);
  const [attachmentFiles, setAttachmentFiles] = useState<File[]>([]);
  const [removingIds, setRemovingIds] = useState<Set<string>>(new Set());
  const [removeModalOpen, setRemoveModalOpen] = useState(false);
  const [docToRemove, setDocToRemove] = useState<DocumentWithUrl | null>(null);

  const hasAnyUpload = useMemo(() => {
    return !!curriculumFile || !!volunteerFile || attachmentFiles.length > 0;
  }, [curriculumFile, volunteerFile, attachmentFiles]);

  const defaultValues: Partial<UpdateFormValues> = {
    nomeCompleto: "",
    email: "",
    documentoProfissional: "",
    areaAtendimento: "",
    telefone: "",
    rg: "",
    estado: "",
    cidade: "",
    bairro: "",
    rua: "",
    numero: "",
    complemento: "",
    cep: "",
    disponibilidade: gerarMatrizDisponibilidade([]),
  };

  const form = useForm<UpdateFormValues>({
    resolver: zodResolver(updateProfessionalSchema),
    defaultValues,
  });

  useEffect(() => {
    if (!profissional) return;

    const disponibilidadesBackend = profissional.availabilities || [];

    const matrizCompleta = gerarMatrizDisponibilidade(
      disponibilidadesBackend.map((a) => ({
        dia: a.day.toLowerCase(),
        turno: a.shift.toLowerCase(),
        checked: true,
      })),
    );

    form.reset({
      nomeCompleto: profissional.name,
      email: profissional.email,
      documentoProfissional: profissional.professionalDocument,
      areaAtendimento: profissional.serviceArea.area,
      telefone: profissional.phoneNumber,
      rg: profissional.identityDocument,
      estado: profissional.address.state,
      cidade: profissional.address.city,
      bairro: profissional.address.neighborhood,
      rua: profissional.address.street,
      numero: profissional.address.number,
      complemento: profissional.address.complement ?? "",
      cep: profissional.address.cep,
      disponibilidade: matrizCompleta,
    });
  }, [profissional, form]);

  async function refreshDocuments(professionalId: string) {
    setDocsLoading(true);
    setDocsError(null);
    try {
      const data = await getProfessionalDocuments(professionalId);
      setDocs(data);
    } catch (e) {
      const error = e as Error;
      setDocsError(error?.message ?? "Erro ao carregar documentos");
    } finally {
      setDocsLoading(false);
    }
  }

  useEffect(() => {
    if (!profissional?.id) return;
    refreshDocuments(profissional.id);

  }, [profissional?.id]);

  const groupedDocs = useMemo(() => {
    const curriculum = docs.find((d) => d.type === "CURRICULUM");
    const volunteer = docs.find((d) => d.type === "VOLUNTEER_AGREEMENT");
    const attachments = docs.filter((d) => d.type === "ATTACHMENTANY");
    return { curriculum, volunteer, attachments };
  }, [docs]);

  function openRemoveModal(doc: DocumentWithUrl) {
    setDocToRemove(doc);
    setRemoveModalOpen(true);
  }

  async function confirmRemove() {
    if (!profissional?.id || !docToRemove) return;

    const idStr = String(docToRemove.id);

    setRemovingIds((prev) => {
      const next = new Set(prev);
      next.add(idStr);
      return next;
    });

    try {
      await removeProfessionalDocument(profissional.id, docToRemove.id);
      await refreshDocuments(profissional.id);
      setRemoveModalOpen(false);
      setDocToRemove(null);
    } catch (e) {
      const error = e as Error;
      console.error(error);
      alert(error?.message ?? "Erro ao remover documento");
    } finally {
      setRemovingIds((prev) => {
        const next = new Set(prev);
        next.delete(idStr);
        return next;
      });
    }
  }

  const onSubmit: SubmitHandler<UpdateFormValues> = async (values) => {
    if (!profissional?.id) return;

    const availabilities = values.disponibilidade
      .filter((d) => d?.checked)
      .map((d) => ({
        day: d?.dia,
        shift: d?.turno,
      }));

    const payload = {
      serviceArea: { area: values.areaAtendimento },
      phoneNumber: values.telefone,
      professionalDocument: values.documentoProfissional.trim(),
      email: values.email.trim(),
      name: values.nomeCompleto.trim(),
      identityDocument: values.rg.trim(),
      address: {
        state: values.estado,
        city: values.cidade.trim(),
        neighborhood: values.bairro.trim(),
        street: values.rua.trim(),
        number: values.numero?.trim(),
        complement: values.complemento?.trim(),
        cep: values.cep,
      },
      availabilities,
    };

    const ok = await updateProfissional(profissional.id, payload);
    if (!ok) return;

    if (hasAnyUpload) {
      const fd = new FormData();

      if (volunteerFile) fd.append("volunteerAgreement", volunteerFile);
      if (curriculumFile) fd.append("curriculum", curriculumFile);
      for (const f of attachmentFiles) fd.append("attachmentAny", f);

      await upload(profissional.id, fd);

      setCurriculumFile(null);
      setVolunteerFile(null);
      setAttachmentFiles([]);
      await refreshDocuments(profissional.id);
    }

    router.push("/visualization-professional");
  };

  const onCancel = () => {
    router.push("/visualization-professional");
  };

  if (loadingProf) return <p>Carregando dados...</p>;
  if (errorProf) return <p className="text-red-500">Erro: {errorProf}</p>;

  const isConfirmBusy =
    !!docToRemove && removingIds.has(String(docToRemove.id));

  return (
    <div className="p-0">
      <AlertDialog open={removeModalOpen} onOpenChange={setRemoveModalOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Você tem certeza?</AlertDialogTitle>
            <AlertDialogDescription>
              Esta ação irá remover o anexo{" "}
              <span className="font-medium">
                {docToRemove?.name ?? "selecionado"}
              </span>
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel disabled={isConfirmBusy}>
              Cancelar
            </AlertDialogCancel>
            <AlertDialogAction
              className="border border-input bg-background text-foreground hover:bg-accent hover:text-accent-foreground"
              onClick={confirmRemove}
              disabled={isConfirmBusy}
            >
              {isConfirmBusy ? "Removendo..." : "Remover"}
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>

      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 w-full max-w-2xl"
        >
          <FormField
            control={form.control}
            name="nomeCompleto"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome completo *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: Maria da Silva" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email *</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="profissional@exemplo.com"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="documentoProfissional"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Documento profissional *</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: CRM/SP 123456" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <Controller
              control={form.control}
              name="areaAtendimento"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Área de atendimento *</FormLabel>
                  <FormControl>
                    <HealthAreaSelect
                      value={field.value}
                      onChange={field.onChange}
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="rg"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RG *</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: 1234567" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <Controller
              control={form.control}
              name="telefone"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Telefone *</FormLabel>
                  <FormControl>
                    <InputMask
                      mask="(__) _____-____"
                      replacement={{ _: /\d/ }}
                      value={field.value ?? ""}
                      onChange={(e) => field.onChange(e.target.value)}
                      onBlur={field.onBlur}
                      placeholder="(xx) xxxxx-xxxx"
                      className="w-full rounded-md border px-3 py-2"
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Controller
              control={form.control}
              name="estado"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>Estado *</FormLabel>
                  <FormControl>
                    <Select onValueChange={field.onChange} value={field.value}>
                      <SelectTrigger
                        className={`w-full ${fieldState.invalid
                            ? "border-red-500"
                            : "border-gray-300"
                          }`}
                      >
                        <SelectValue placeholder="Selecione um estado" />
                      </SelectTrigger>
                      <SelectContent>
                        {STATES.map((s) => (
                          <SelectItem key={s} value={s}>
                            {s}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="cidade"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Cidade *</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: João Pessoa" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <FormField
            control={form.control}
            name="rua"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Endereço *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: Rua das Flores" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <FormField
              control={form.control}
              name="bairro"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Bairro *</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Centro" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <Controller
              control={form.control}
              name="cep"
              render={({ field, fieldState }) => (
                <FormItem>
                  <FormLabel>CEP *</FormLabel>
                  <FormControl>
                    <InputMask
                      mask="_____-___"
                      replacement={{ _: /\d/ }}
                      value={field.value ?? ""}
                      onChange={(e) => field.onChange(e.target.value)}
                      onBlur={field.onBlur}
                      placeholder="12345-678"
                      className="w-full rounded-md border px-3 py-2"
                    />
                  </FormControl>
                  <FormMessage>{fieldState.error?.message}</FormMessage>
                </FormItem>
              )}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <FormField
              control={form.control}
              name="numero"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Número *</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: 123" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="complemento"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Complemento</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Apt 101" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>

          <Disponibilidade control={form.control} watch={form.watch} />

          <div className="space-y-4">
            <div className="rounded-md border p-4 space-y-2">
              <p className="text-base font-semibold">Documentos já anexados</p>

              {docsLoading ? (
                <p className="text-sm text-gray-600">
                  Carregando documentos...
                </p>
              ) : docsError ? (
                <p className="text-sm text-red-500">{docsError}</p>
              ) : (
                <div className="text-sm text-gray-700 space-y-2">
                  <p>
                    <span className="font-medium">Termo do voluntário: </span>
                    {groupedDocs.volunteer ? (
                      <a
                        className="text-[#0D4F97] hover:underline"
                        href={groupedDocs.volunteer.url}
                        target="_blank"
                        rel="noreferrer"
                      >
                        Visualizar
                      </a>
                    ) : (
                      <span className="text-gray-500">não enviado</span>
                    )}
                  </p>

                  <p>
                    <span className="font-medium">Currículo: </span>
                    {groupedDocs.curriculum ? (
                      <a
                        className="text-[#0D4F97] hover:underline"
                        href={groupedDocs.curriculum.url}
                        target="_blank"
                        rel="noreferrer"
                      >
                        Visualizar
                      </a>
                    ) : (
                      <span className="text-gray-500">não enviado</span>
                    )}
                  </p>

                  <div>
                    <p>
                      <span className="font-medium">Anexos: </span>
                      {groupedDocs.attachments.length > 0 ? (
                        <span>{groupedDocs.attachments.length} arquivo(s)</span>
                      ) : (
                        <span className="text-gray-500">nenhum</span>
                      )}
                    </p>

                    {groupedDocs.attachments.length > 0 && (
                      <ul className="mt-2 space-y-2">
                        {groupedDocs.attachments.map((a) => {
                          const busy = removingIds.has(String(a.id));

                          return (
                            <li
                              key={a.id}
                              className="flex items-center justify-between rounded-md border px-3 py-2"
                            >
                              <div className="min-w-0">
                                <p className="truncate text-sm text-gray-700">
                                  {a.name}
                                </p>
                              </div>

                              <div className="flex items-center gap-2">
                                <a
                                  className="text-[#0D4F97] hover:underline text-sm"
                                  href={a.url}
                                  target="_blank"
                                  rel="noreferrer"
                                >
                                  Visualizar
                                </a>
                                <Button
                                  type="button"
                                  variant="outline"
                                  size="icon"
                                  className="h-8 w-8 text-muted-foreground hover:text-foreground"
                                  disabled={busy || loadingDocs || docsLoading}
                                  onClick={() => openRemoveModal(a)}
                                  aria-label="Remover anexo"
                                  title="Remover anexo"
                                >
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </div>
                            </li>
                          );
                        })}
                      </ul>
                    )}
                  </div>
                </div>
              )}
            </div>

            <FormItem>
              <FormLabel>Termo do Voluntário</FormLabel>
              <FormControl>
                <Input
                  type="file"
                  accept="application/pdf"
                  onChange={(e) =>
                    setVolunteerFile(e.target.files?.[0] ?? null)
                  }
                />
              </FormControl>
              {volunteerFile && (
                <p className="text-xs text-gray-600 mt-1">
                  Selecionado: {volunteerFile.name}
                </p>
              )}
            </FormItem>

            <FormItem>
              <FormLabel>Currículo</FormLabel>
              <FormControl>
                <Input
                  type="file"
                  accept="application/pdf"
                  onChange={(e) =>
                    setCurriculumFile(e.target.files?.[0] ?? null)
                  }
                />
              </FormControl>
              {curriculumFile && (
                <p className="text-xs text-gray-600 mt-1">
                  Selecionado: {curriculumFile.name}
                </p>
              )}
            </FormItem>

            <FormItem>
              <FormLabel>Anexo qualquer</FormLabel>
              <FormControl>
                <Input
                  type="file"
                  accept="application/pdf"
                  multiple
                  onChange={(e) => {
                    const list = Array.from(e.target.files ?? []);
                    setAttachmentFiles(list);
                  }}
                />
              </FormControl>
              {attachmentFiles.length > 0 && (
                <p className="text-xs text-gray-600 mt-1">
                  Selecionado(s): {attachmentFiles.length} arquivo(s)
                </p>
              )}
            </FormItem>

            {(errorDocs || successDocs) && (
              <div className="text-sm">
                {errorDocs && <p className="text-red-500">{errorDocs}</p>}
                {successDocs && (
                  <p className="text-green-600">
                    Documentos enviados com sucesso!
                  </p>
                )}
              </div>
            )}

            {hasAnyUpload && (
              <p className="text-xs text-gray-600">
                Ao salvar, também serão enviados os documentos selecionados.
              </p>
            )}
          </div>

          {(loading || loadingDocs) && (
            <p className="text-blue-500">
              {loading ? "Salvando perfil..." : "Enviando documentos..."}
            </p>
          )}
          {error && <p className="text-red-500">{error}</p>}
          {success && (
            <p className="text-green-600">
              Profissional atualizado com sucesso!
            </p>
          )}

          <div className="flex justify-end gap-4">
            <Button type="button" variant="outline" onClick={onCancel}>
              Cancelar
            </Button>

            <Button
              type="submit"
              className="bg-[#0D4F97] hover:bg-blue-900"
              disabled={form.formState.isSubmitting || loading || loadingDocs}
            >
              Salvar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

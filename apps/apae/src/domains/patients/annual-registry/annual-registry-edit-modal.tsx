"use client";

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Loader2, Upload, FileText, RefreshCw, ExternalLink } from "lucide-react";
import { StringMultiSelect } from "@/components/StringMultiSelect";
import { GenericDatabaseSelect } from "@/components/GenericDatabaseSelect";
import { useAnnualRegistryModal, availableYears } from "./use-annual-registry-modal";
import { MEDICAL_DOC_TYPES, DOC_TYPE_TRANSLATIONS, type AnnualRegistry } from "./annual-registry.types";

interface AnnualRegistryEditModalProps {
  isOpen: boolean;
  onClose: (str?: string) => void;
  patientId: string;
  currentYear: string;
  initialData: AnnualRegistry | null;
  mode?: "create" | "edit";
}

export default function AnnualRegistryEditModal({
  isOpen,
  onClose,
  patientId,
  currentYear,
  initialData,
  mode = "edit",
}: AnnualRegistryEditModalProps) {
  const {
    form,
    documents,
    isLoadingDocs,
    isUploading,
    docType,
    setDocType,
    fileInputRef,
    handleFileUpload,
    handleMoneyChange,
    onSubmit,
    loadDocuments,
  } = useAnnualRegistryModal({ isOpen, patientId, currentYear, initialData, mode, onClose });

  const { isSubmitting, errors } = form.formState;
  const isCreateMode = mode === "create";

  return (
    <Dialog open={isOpen} onOpenChange={() => onClose()}>
      <DialogContent className="!max-w-[1200px] w-[95vw] h-[85vh] flex flex-col p-0 overflow-hidden bg-slate-50 rounded-xl shadow-xl">
        <DialogHeader className="px-6 py-4 bg-[#0D4F97] text-white shrink-0">
          <DialogTitle className="text-xl font-bold font-baloo">
            {isCreateMode ? "Novo Registro Anual" : "Edição de Saúde & Social"}
          </DialogTitle>
          <p className="text-blue-200 text-xs mt-0.5 opacity-90">
            {isCreateMode ? "Preencha os dados para iniciar um novo ano." : `Referência: ${currentYear}`}
          </p>
        </DialogHeader>

        <div className="flex-1 overflow-y-auto p-5 pb-24">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6 h-full">
            <div className="bg-white p-5 rounded-xl shadow-sm border border-slate-200 h-fit">
              <h3 className="text-[#0D4F97] font-bold text-base mb-4 pb-2 border-b border-slate-100 flex items-center gap-2">
                <span className="bg-blue-50 p-1.5 rounded-lg text-[#0D4F97]"><FileText className="h-4 w-4" /></span>
                Dados Clínicos e Sociais
              </h3>
              <Form {...form}>
                <form id="health-form" onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                  {isCreateMode && (
                    <FormField control={form.control} name="year" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.year ? "text-red-500" : "text-slate-700"}`}>Ano de Referência</FormLabel>
                        <Select onValueChange={field.onChange} defaultValue={field.value}>
                          <FormControl>
                            <SelectTrigger className={`bg-slate-50 h-10 text-sm ${errors.year ? "border-red-500 ring-red-500" : "border-slate-200"}`}>
                              <SelectValue placeholder="Selecione o ano" />
                            </SelectTrigger>
                          </FormControl>
                          <SelectContent className="max-h-60">
                            {availableYears.map((year) => (
                              <SelectItem key={year} value={year}>{year}</SelectItem>
                            ))}
                          </SelectContent>
                        </Select>
                        <FormMessage className="text-red-500 text-[10px]" />
                      </FormItem>
                    )} />
                  )}

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <FormField control={form.control} name="bpc" render={({ field }) => (
                      <FormItem>
                        <FormLabel className="text-slate-700 font-bold text-xs">Recebe BPC?</FormLabel>
                        <Select onValueChange={field.onChange} value={field.value}>
                          <FormControl><SelectTrigger className="bg-slate-50 border-slate-200 h-10 text-sm"><SelectValue placeholder="Selecione" /></SelectTrigger></FormControl>
                          <SelectContent>
                            <SelectItem value="true">Sim</SelectItem>
                            <SelectItem value="false">Não</SelectItem>
                          </SelectContent>
                        </Select>
                        <FormMessage />
                      </FormItem>
                    )} />

                    <FormField control={form.control} name="familyIncome" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.familyIncome ? "text-red-500" : "text-slate-700"}`}>Renda Familiar</FormLabel>
                        <FormControl>
                          <Input
                            {...field}
                            className={`bg-slate-50 h-10 text-sm ${errors.familyIncome ? "border-red-500 focus-visible:ring-red-500" : "border-slate-200"}`}
                            placeholder="R$ 0,00"
                            value={field.value}
                            onChange={(e) => handleMoneyChange(e, field.onChange)}
                          />
                        </FormControl>
                        <FormMessage className="text-red-500 text-[10px]" />
                      </FormItem>
                    )} />
                  </div>

                  <div className="space-y-3">
                    <FormField control={form.control} name="diseases" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.diseases ? "text-red-500" : "text-slate-700"}`}>Doenças</FormLabel>
                        <FormControl>
                          <div className={errors.diseases ? "border-red-500 border rounded-md" : ""}>
                            <StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite doenças..." />
                          </div>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />

                    <FormField control={form.control} name="allergies" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.allergies ? "text-red-500" : "text-slate-700"}`}>Alergias</FormLabel>
                        <FormControl>
                          <div className={errors.allergies ? "border-red-500 border rounded-md" : ""}>
                            <StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite alergias..." />
                          </div>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />

                    <FormField control={form.control} name="continuousMedication" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.continuousMedication ? "text-red-500" : "text-slate-700"}`}>Medicamentos</FormLabel>
                        <FormControl>
                          <div className={errors.continuousMedication ? "border-red-500 border rounded-md" : ""}>
                            <StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite medicamentos..." />
                          </div>
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />

                    <FormField control={form.control} name="vaccines" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.vaccines ? "text-red-500" : "text-slate-700"}`}>Vacinas</FormLabel>
                        <FormControl>
                          <GenericDatabaseSelect value={field.value || []} onChange={field.onChange} endpoint="/apae-geral/api/vaccines" labelSingular="Vacina" labelKey="name" placeholder="Selecione vacinas..." />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />
                  </div>

                  <div className="pt-1 space-y-3">
                    <FormField control={form.control} name="disorders" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.disorders ? "text-red-500" : "text-slate-700"} mb-1.5 block`}>Transtornos</FormLabel>
                        <FormControl>
                          <GenericDatabaseSelect value={field.value || []} onChange={field.onChange} endpoint="/apae-geral/api/disorders" labelSingular="Transtorno" labelKey="name" placeholder="Selecione transtornos..." />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />

                    <FormField control={form.control} name="serviceTypes" render={({ field }) => (
                      <FormItem>
                        <FormLabel className={`font-bold text-xs ${errors.serviceTypes ? "text-red-500" : "text-slate-700"} mb-1.5 block`}>Tipos de Atendimento</FormLabel>
                        <FormControl>
                          <GenericDatabaseSelect value={field.value || []} onChange={field.onChange} endpoint="/apae-geral/api/service-types" labelSingular="Tipo" placeholder="Selecione tipos..." labelKey="area" menuPlacement="top" />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )} />
                  </div>
                </form>
              </Form>
            </div>

            <div className="flex flex-col h-full bg-white p-5 rounded-xl shadow-sm border border-slate-200">
              <h3 className="text-[#0D4F97] font-bold text-base mb-4 pb-2 border-b border-slate-100 flex items-center justify-between">
                <div className="flex items-center gap-2">
                  <span className="bg-green-50 p-1.5 rounded-lg text-green-700"><FileText className="h-4 w-4" /></span>
                  Documentação Digital
                </div>
                <Button variant="ghost" size="sm" onClick={loadDocuments} disabled={isLoadingDocs}>
                  <RefreshCw className={`h-4 w-4 ${isLoadingDocs ? "animate-spin" : ""}`} />
                </Button>
              </h3>

              <div className="flex-1 flex flex-col">
                {isCreateMode ? (
                  <div className="flex flex-col items-center justify-center h-full text-center p-8 opacity-60">
                    <FileText className="h-12 w-12 text-slate-300 mb-3" />
                    <p className="text-slate-500 font-medium text-sm">Documentos poderão ser anexados após a criação do registro.</p>
                  </div>
                ) : (
                  <>
                    <div className="bg-slate-50 p-5 rounded-xl border-2 border-dashed border-slate-300 mb-6 group">
                      <label className="text-xs font-bold text-slate-500 uppercase mb-3 block text-center tracking-widest group-hover:text-[#0D4F97]">Adicionar Novo Documento</label>
                      <div className="flex flex-col gap-3 max-w-sm mx-auto w-full">
                        <Select value={docType} onValueChange={setDocType}>
                          <SelectTrigger className="w-full bg-white border-slate-200 h-10 text-sm"><SelectValue /></SelectTrigger>
                          <SelectContent>
                            {MEDICAL_DOC_TYPES.map((t) => <SelectItem key={t.value} value={t.value}>{t.label}</SelectItem>)}
                          </SelectContent>
                        </Select>
                        <input type="file" ref={fileInputRef} onChange={handleFileUpload} className="hidden" accept=".pdf,.jpg,.png,.jpeg" disabled={isUploading} />
                        <Button variant="outline" className="w-full bg-white text-[#0D4F97] border-[#0D4F97]/20 hover:bg-[#0D4F97] hover:text-white h-10 transition-all text-sm" onClick={() => fileInputRef.current?.click()} disabled={isUploading}>
                          {isUploading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : <Upload className="mr-2 h-4 w-4" />}
                          Selecionar Arquivo
                        </Button>
                      </div>
                    </div>

                    <div className="flex-1 overflow-y-auto max-h-[400px] pr-2">
                      <h4 className="text-[10px] font-bold text-slate-400 uppercase mb-3 tracking-widest">Arquivos Anexados ({documents.length})</h4>
                      <div className="space-y-2">
                        {documents.length === 0 ? (
                          <div className="flex flex-col items-center justify-center h-32 text-slate-400 border-2 border-slate-100 rounded-xl bg-slate-50/50">
                            <FileText className="h-8 w-8 mb-2 opacity-20" />
                            <p className="text-xs font-medium opacity-60">Nenhum documento encontrado.</p>
                          </div>
                        ) : (
                          documents.map((doc) => (
                            <div key={doc.id} className="group flex items-center justify-between p-3 bg-white border border-slate-100 rounded-xl shadow-sm hover:border-[#0D4F97]/20 transition-all duration-200">
                              <div className="flex items-center gap-3 overflow-hidden">
                                <div className="bg-blue-50 p-2 rounded-lg group-hover:bg-[#0D4F97] group-hover:text-white transition-colors duration-300">
                                  <FileText className="h-4 w-4" />
                                </div>
                                <div className="flex flex-col min-w-0">
                                  <span className="text-sm font-semibold truncate text-slate-700 group-hover:text-[#0D4F97] transition-colors" title={doc.name}>{doc.name}</span>
                                  <span className="text-[10px] text-slate-400 font-bold uppercase tracking-wide mt-0.5">{DOC_TYPE_TRANSLATIONS[doc.type] || doc.type}</span>
                                </div>
                              </div>
                              {doc.url && (
                                <a href={doc.url} target="_blank" rel="noreferrer" className="p-2 text-slate-400 hover:text-[#0D4F97] hover:bg-blue-50 rounded-full transition-all">
                                  <ExternalLink className="h-4 w-4" />
                                </a>
                              )}
                            </div>
                          ))
                        )}
                      </div>
                    </div>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>

        <DialogFooter className="px-6 py-4 bg-white border-t border-slate-100 shrink-0 flex justify-end gap-3 z-10">
          <Button variant="ghost" onClick={() => onClose()} type="button" className="text-slate-500 h-10 px-5 text-sm">Cancelar</Button>
          <Button form="health-form" type="submit" disabled={isSubmitting} className="text-white bg-[#0D4F97] hover:bg-[#0b427d] h-10 px-6 rounded-lg font-bold transition-all text-sm disabled:opacity-70">
            {isSubmitting ? <><Loader2 className="mr-2 h-4 w-4 animate-spin" /> Salvando...</> : (isCreateMode ? "Criar Registro" : "Salvar Alterações")}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

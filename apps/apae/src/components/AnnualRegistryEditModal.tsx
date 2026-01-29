"use client";

import { useEffect, useState, useRef } from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AnnualRegistryFormSchema, AnnualRegistryFormValues } from "@/schemas/anualRegistrySchema"; 
import { toast } from "react-toastify"; 
import { Loader2, Upload, FileText, RefreshCw, ExternalLink } from "lucide-react"; 

import { StringMultiSelect } from "@/components/StringMultiSelect";
import { GenericDatabaseSelect } from "@/components/GenericDatabaseSelect";

interface DocumentDTO {
    id: string;
    name: string;
    category: string;
    type: string;
    url: string;
}

interface AnnualRegistryEditModalProps {
    isOpen: boolean;
    onClose: (str?: string) => void;
    patientId: string;
    currentYear: string; 
    initialData: any;    
    mode?: "create" | "edit"; 
}

const MEDICAL_DOC_TYPES = [
    { value: "MEDICAL_REPORT", label: "Laudo Médico" },
    { value: "EXAMINATION", label: "Exame" },
    { value: "REFERRAL", label: "Encaminhamento" },
    { value: "OTHER", label: "Outro" }
];

export default function AnnualRegistryEditModal({ 
    isOpen, 
    onClose, 
    patientId, 
    currentYear, 
    initialData,
    mode = "edit" 
}: AnnualRegistryEditModalProps) {
    
    const [documents, setDocuments] = useState<DocumentDTO[]>([]);
    const [isLoadingDocs, setIsLoadingDocs] = useState(false);
    const [isUploading, setIsUploading] = useState(false);
    const [docType, setDocType] = useState("MEDICAL_REPORT");
    
    const [fullPatientData, setFullPatientData] = useState<any>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);

    const currentYearInt = new Date().getFullYear();

    const availableYears = Array.from({ length: 32 }, (_, i) => (currentYearInt + 1 - i).toString());

    const form = useForm<AnnualRegistryFormValues>({
        resolver: zodResolver(AnnualRegistryFormSchema),
        defaultValues: {
            year: currentYear || currentYearInt.toString(),
            bpc: "false", 
            familyIncome: "",
            diseases: "",
            continuousMedication: "",
            disorders: [],     
            allergies: "",
            vaccines: [],      
            serviceTypes: []   
        },
    });

    const { isSubmitting } = form.formState;

    useEffect(() => {
        if (isOpen && patientId) {
            if (mode === "edit") fetchDocuments();
            else setDocuments([]);
            fetchPatientData(); 
        }
    }, [isOpen, patientId, mode]);

    useEffect(() => {
        if (isOpen) {
            if (mode === "edit" && initialData) {
                const rawBpc = initialData.bpc;
                const bpcString = (rawBpc === true || String(rawBpc) === "true") ? "true" : "false";

                const vaccineList = Array.isArray(fullPatientData?.vaccineNames) 
                    ? fullPatientData.vaccineNames.map((v: any) => (typeof v === 'string' ? { name: v } : v)) 
                    : [];

                const sourceServiceAreas = initialData.serviceArea || initialData.serviceAreas || initialData.serviceTypes || [];
                const serviceTypeList = Array.isArray(sourceServiceAreas) ? sourceServiceAreas.map((s: any) => ({
                    id: s.id,
                    area: s.area || s.name, 
                    name: s.name || s.area  
                })) : [];

                const disorderList = Array.isArray(initialData.disorders) ? initialData.disorders : [];

                form.reset({
                    year: currentYear, 
                    bpc: bpcString,
                    familyIncome: initialData.familyIncome ? formatCurrencyForDisplay(initialData.familyIncome) : "",
                    diseases: initialData.diseases ?? "",
                    continuousMedication: initialData.continuousMedication ?? "",
                    allergies: fullPatientData?.allergies ?? "",
                    disorders: disorderList, 
                    vaccines: vaccineList,
                    serviceTypes: serviceTypeList 
                });

            } else if (mode === "create") {
                const vaccineList = Array.isArray(fullPatientData?.vaccineNames) 
                    ? fullPatientData.vaccineNames.map((v: any) => (typeof v === 'string' ? { name: v } : v)) 
                    : [];

                form.reset({
                    year: currentYearInt.toString(),
                    bpc: "false",
                    familyIncome: "",
                    diseases: "",
                    continuousMedication: "",
                    allergies: fullPatientData?.allergies ?? "",
                    disorders: [],
                    vaccines: vaccineList,
                    serviceTypes: []
                });
            }
        }
    }, [initialData, fullPatientData, isOpen, form, mode, currentYear]);
    
    const fetchDocuments = async () => {
        setIsLoadingDocs(true);
        try {
            const response = await fetch(`/api/pessoas/${patientId}/documentos?category=medicos`);
            if (response.ok) {
                const data = await response.json().catch(() => []);
                setDocuments(Array.isArray(data) ? data : []);
            }
        } catch (error) { console.error("Erro fetch docs:", error); } 
        finally { setIsLoadingDocs(false); }
    };

    const fetchPatientData = async () => {
        try {
            const response = await fetch(`/api/pessoas/${patientId}`);
            if (response.ok) setFullPatientData(await response.json());
        } catch (error) { console.error(error); }
    };

    const handleFileUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;
        setIsUploading(true);
        const formData = new FormData();
        formData.append("file", file);
        formData.append("category", "MEDICAL");
        formData.append("type", docType);
        try {
            const res = await fetch(`/api/pessoas/${patientId}/documentos`, { method: "POST", body: formData });
            if (!res.ok) throw new Error("Falha no upload");
            toast.success("Documento anexado!");
            fetchDocuments();
            if (fileInputRef.current) fileInputRef.current.value = "";
        } catch (error) { toast.error("Erro ao enviar documento."); } 
        finally { setIsUploading(false); }
    };

    const cleanCurrency = (value: string) => (!value ? "0.00" : value.replace(/[^\d,]/g, '').replace(',', '.'));
    const formatCurrencyForDisplay = (value: number | string) => (!value ? "" : Number(value).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' }));
    
    const handleMoneyChange = (e: React.ChangeEvent<HTMLInputElement>, onChange: (value: string) => void) => {
        let value = e.target.value.replace(/\D/g, "");
        if (value === "") { onChange(""); return; }
        onChange((parseFloat(value) / 100).toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
    };

    const cleanPatientData = (data: any) => {
        if (!data) return {};
        const { documents, annualRegistry, createdAt, updatedAt, deleted, isDeleted, age, ...rest } = data;
        return rest;
    };

    const onSubmit = async (data: AnnualRegistryFormValues) => { 
        try {
            const registryId = initialData?.id; 
            
            const finalDiseases = (data.diseases && data.diseases.trim() !== "") ? data.diseases : "Nenhuma";
            const finalMedication = (data.continuousMedication && data.continuousMedication.trim() !== "") ? data.continuousMedication : "Nenhum";
            const finalAllergies = (data.allergies && data.allergies.trim() !== "") ? data.allergies : "Nenhuma";
            
            const income = parseFloat(cleanCurrency(data.familyIncome));
            const bpcToSend = data.bpc === "true"; 

            const formattedDisorders = (data.disorders || []).map((d: any) => ({ 
                name: d.name || d.label || d.value,
                id: d.id 
            }));

            const formattedServiceAreas = (data.serviceTypes || []).map((s: any) => ({
                id: s.id,
                area: s.area || s.name || s.label || s.value 
            }));

            const regPayload: any = {
                bpc: bpcToSend, 
                familyIncome: income,
                diseases: finalDiseases, 
                continuousMedication: finalMedication, 
                disorders: formattedDisorders,
                serviceArea: formattedServiceAreas,  
                serviceAreas: formattedServiceAreas 
            };

            let regRes;

            if (mode === "create") {
                regPayload.year = parseInt(data.year);

                regRes = await fetch(`/api/pessoas/${patientId}/registro-anual`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(regPayload),
                });
            } else {
                if (!registryId) throw new Error("ID do registro não encontrado.");
                
                regRes = await fetch(`/api/pessoas/${patientId}/registro-anual/${registryId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(regPayload),
                });
            }

            if (!regRes.ok) {
                 const errorText = await regRes.text();
                 console.error("Erro Backend:", errorText);
                 if (regRes.status === 409) {
                    throw new Error(`Já existe um registro para o ano de ${data.year}.`);
                 }
                 throw new Error("Erro ao salvar registro anual.");
            }

            if (fullPatientData) {
                const vaccineList = (data.vaccines || []).map((v: any) => ({ name: v.name || v.label || v.value, id: v.id }));
                const baseData = cleanPatientData(fullPatientData);
                const safeNationality = baseData.nationality || baseData.birthplace || "Brasileira";

                const patientPayload = {
                    ...baseData,
                    nationality: safeNationality,
                    allergies: finalAllergies,
                    vaccineNames: vaccineList,
                    address: fullPatientData.address ? { ...fullPatientData.address } : null,
                    guardian: fullPatientData.guardian ? { ...fullPatientData.guardian } : null,
                    parents: fullPatientData.parents?.map((p: any) => ({
                        id: p.id, name: p.name, rg: p.rg, cpf: p.cpf, profession: p.profession, isAlive: p.isAlive, kinship: p.kinship
                    })) ?? []
                };

                await fetch(`/api/pessoas/${patientId}`, {
                    method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(patientPayload)
                });
            }

            const savedYear = mode === "create" ? data.year : undefined;
            onClose(savedYear);
            
            toast.success(mode === "create" ? "Registro criado com sucesso!" : "Alterações salvas!");

        } catch (error: any) {
            console.error(error);
            toast.error(error.message || "Erro ao salvar.");
        }
    };

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
                            <h3 className="text-[#0D4F97] font-bold text-base mb-4 pb-2 border-b border-slate-100 flex items-center gap-2"><span className="bg-blue-50 p-1.5 rounded-lg text-[#0D4F97]"><FileText className="h-4 w-4" /></span>Dados Clínicos e Sociais</h3>
                            <Form {...form}>
                                <form id="health-form" onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                                    
                                    {isCreateMode && (
                                        <FormField control={form.control} name="year" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-bold text-xs">Ano de Referência</FormLabel>
                                                <Select onValueChange={field.onChange} defaultValue={field.value}>
                                                    <FormControl><SelectTrigger className="bg-slate-50 border-slate-200 h-10 text-sm"><SelectValue placeholder="Selecione o ano" /></SelectTrigger></FormControl>
                                                    <SelectContent className="max-h-60">
                                                        {availableYears.map(year => (
                                                            <SelectItem key={year} value={year}>{year}</SelectItem>
                                                        ))}
                                                    </SelectContent>
                                                </Select>
                                                <FormMessage />
                                            </FormItem>
                                        )} />
                                    )}

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <FormField control={form.control} name="bpc" render={({ field }) => (
                                            <FormItem><FormLabel className="text-slate-700 font-bold text-xs">Recebe BPC?</FormLabel>
                                                <Select 
                                                    onValueChange={field.onChange} 
                                                    value={field.value} 
                                                    defaultValue={field.value}
                                                >
                                                    <FormControl><SelectTrigger className="bg-slate-50 border-slate-200 h-10 text-sm"><SelectValue placeholder="Selecione" /></SelectTrigger></FormControl>
                                                    <SelectContent><SelectItem value="true">Sim</SelectItem><SelectItem value="false">Não</SelectItem></SelectContent>
                                                </Select><FormMessage />
                                            </FormItem>)} />
                                        <FormField control={form.control} name="familyIncome" render={({ field }) => (
                                            <FormItem><FormLabel className="text-slate-700 font-bold text-xs">Renda Familiar</FormLabel><FormControl><Input className="bg-slate-50 border-slate-200 h-10 text-sm" placeholder="R$ 0,00" value={field.value} onChange={(e) => handleMoneyChange(e, field.onChange)} /></FormControl><FormMessage /></FormItem>)} />
                                    </div>
                                    <div className="space-y-3">
                                        <FormField control={form.control} name="diseases" render={({ field }) => (
                                            <FormItem><FormLabel className="text-slate-700 font-bold text-xs">Doenças</FormLabel>
                                            <FormControl><StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite doenças (ex: Diabetes)..." /></FormControl><FormMessage /></FormItem>)} />
                                        
                                        <FormField control={form.control} name="allergies" render={({ field }) => (
                                            <FormItem><FormLabel className="text-slate-700 font-bold text-xs">Alergias</FormLabel>
                                            <FormControl><StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite alergias (ex: Dipirona)..." /></FormControl><FormMessage /></FormItem>)} />
                                        
                                        <FormField control={form.control} name="continuousMedication" render={({ field }) => (
                                            <FormItem><FormLabel className="text-slate-700 font-bold text-xs">Medicamentos</FormLabel>
                                            <FormControl><StringMultiSelect value={field.value || ""} onChange={field.onChange} placeholder="Digite medicamentos..." /></FormControl><FormMessage /></FormItem>)} />
                                        
                                        <FormField control={form.control} name="vaccines" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-bold text-xs">Vacinas</FormLabel>
                                                <FormControl>
                                                    <GenericDatabaseSelect 
                                                        value={field.value || []} 
                                                        onChange={field.onChange}
                                                        endpoint="/api/vacinas"
                                                        labelSingular="Vacina"
                                                        placeholder="Selecione ou crie vacinas..."
                                                    />
                                                </FormControl><FormMessage /></FormItem>)} 
                                        />
                                    </div>
                                    <div className="pt-1 space-y-3">
                                        <FormField control={form.control} name="disorders" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-bold text-xs mb-1.5 block">Transtornos</FormLabel>
                                                <FormControl>
                                                    <GenericDatabaseSelect 
                                                        value={field.value || []} 
                                                        onChange={field.onChange}
                                                        endpoint="/api/transtornos"
                                                        labelSingular="Transtorno"
                                                        placeholder="Selecione ou crie transtornos..."
                                                    />
                                                </FormControl><FormMessage /></FormItem>)} 
                                        />

                                        <FormField control={form.control} name="serviceTypes" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-bold text-xs mb-1.5 block">Tipos de Atendimento</FormLabel>
                                                <FormControl>
                                                    <GenericDatabaseSelect 
                                                        value={field.value || []} 
                                                        onChange={field.onChange}
                                                        endpoint="/api/tipo-atendimento" 
                                                        labelSingular="Tipo de Atendimento"
                                                        placeholder="Selecione ou crie tipos..."
                                                        labelKey="area" 
                                                        menuPlacement="top"
                                                    />
                                                </FormControl><FormMessage /></FormItem>)} 
                                        />
                                    </div>
                                </form>
                            </Form>
                        </div>
                        <div className="flex flex-col h-full bg-white p-5 rounded-xl shadow-sm border border-slate-200">
                             <h3 className="text-[#0D4F97] font-bold text-base mb-4 pb-2 border-b border-slate-100 flex items-center justify-between">
                                <div className="flex items-center gap-2"><span className="bg-green-50 p-1.5 rounded-lg text-green-700"><FileText className="h-4 w-4" /></span>Documentação Digital</div>
                                <Button variant="ghost" size="sm" className="h-8 w-8 p-0 hover:bg-slate-100 rounded-full" onClick={fetchDocuments} disabled={isLoadingDocs}><RefreshCw className={`h-4 w-4 ${isLoadingDocs ? 'animate-spin' : ''}`} /></Button>
                            </h3>
                            <div className="flex-1 flex flex-col">
                                {isCreateMode ? (
                                    <div className="flex flex-col items-center justify-center h-full text-center p-8 opacity-60">
                                        <FileText className="h-12 w-12 text-slate-300 mb-3" />
                                        <p className="text-slate-500 font-medium text-sm">Documentos poderão ser anexados após a criação do registro.</p>
                                    </div>
                                ) : (
                                    <>
                                        <div className="bg-slate-50 p-5 rounded-xl border-2 border-dashed border-slate-300 mb-6 hover:border-[#0D4F97]/40 transition-all duration-300 group">
                                            <label className="text-xs font-bold text-slate-500 uppercase mb-3 block text-center tracking-widest group-hover:text-[#0D4F97] transition-colors">Adicionar Novo Documento</label>
                                            <div className="flex flex-col gap-3 max-w-sm mx-auto w-full">
                                                <Select value={docType} onValueChange={setDocType}>
                                                    <SelectTrigger className="w-full bg-white shadow-sm border-slate-200 h-10 text-sm"><SelectValue /></SelectTrigger>
                                                    <SelectContent>{MEDICAL_DOC_TYPES.map(t => <SelectItem key={t.value} value={t.value}>{t.label}</SelectItem>)}</SelectContent>
                                                </Select>
                                                <input type="file" ref={fileInputRef} onChange={handleFileUpload} className="hidden" accept=".pdf,.jpg,.png,.jpeg" disabled={isUploading} />
                                                <Button variant="outline" className="w-full bg-white text-[#0D4F97] border-[#0D4F97]/20 hover:bg-[#0D4F97] hover:text-white shadow-sm h-10 transition-all text-sm" onClick={() => fileInputRef.current?.click()} disabled={isUploading}>{isUploading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : <Upload className="mr-2 h-4 w-4"/>} Selecionar Arquivo</Button>
                                            </div>
                                        </div>
                                        <div className="flex-1 overflow-y-auto max-h-[400px] pr-2 custom-scrollbar">
                                            <h4 className="text-[10px] font-bold text-slate-400 uppercase mb-3 tracking-widest">Arquivos Anexados ({documents.length})</h4>
                                            <div className="space-y-2">
                                                {documents.length === 0 ? (
                                                    <div className="flex flex-col items-center justify-center h-32 text-slate-400 border-2 border-slate-100 rounded-xl bg-slate-50/50"><FileText className="h-8 w-8 mb-2 opacity-20" /><p className="text-xs font-medium opacity-60">Nenhum documento encontrado.</p></div>
                                                ) : (
                                                    documents.map((doc) => (
                                                        <div key={doc.id} className="group flex items-center justify-between p-3 bg-white border border-slate-100 rounded-xl shadow-sm hover:shadow-md hover:border-[#0D4F97]/20 transition-all duration-200">
                                                            <div className="flex items-center gap-3 overflow-hidden">
                                                                <div className="bg-blue-50 p-2 rounded-lg group-hover:bg-[#0D4F97] group-hover:text-white transition-colors duration-300"><FileText className="h-4 w-4" /></div>
                                                                <div className="flex flex-col min-w-0"><span className="text-sm font-semibold truncate text-slate-700 group-hover:text-[#0D4F97] transition-colors" title={doc.name}>{doc.name}</span><span className="text-[10px] text-slate-400 font-bold uppercase tracking-wide mt-0.5">{doc.type}</span></div>
                                                            </div>
                                                            {doc.url && (<a href={doc.url} target="_blank" rel="noreferrer" className="p-2 text-slate-400 hover:text-[#0D4F97] hover:bg-blue-50 rounded-full transition-all" title="Abrir em nova aba"><ExternalLink className="h-4 w-4" /></a>)}
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
                <DialogFooter className="px-6 py-4 bg-white border-t border-slate-100 shrink-0 flex justify-end gap-3 shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.05)] z-10">
                    <Button variant="ghost" onClick={() => onClose()} type="button" className="text-slate-500 hover:text-slate-800 hover:bg-slate-100 h-10 px-5 rounded-lg font-medium transition-colors text-sm">Cancelar</Button>
                    <Button 
                        form="health-form" 
                        type="submit" 
                        disabled={isSubmitting}
                        className="text-white bg-[#0D4F97] hover:bg-[#0b427d] shadow-lg shadow-blue-900/10 h-10 px-6 rounded-lg font-bold tracking-wide transition-all transform active:scale-95 text-sm disabled:opacity-70 disabled:cursor-not-allowed"
                    >
                        {isSubmitting ? <><Loader2 className="mr-2 h-4 w-4 animate-spin" /> {isCreateMode ? "Criar Registro" : "Salvar Alterações"}</> : (isCreateMode ? "Criar Registro" : "Salvar Alterações")}
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}

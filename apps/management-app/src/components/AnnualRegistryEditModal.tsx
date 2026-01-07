"use client";

import { useEffect, useState, useRef } from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AnnualRegistryFormSchema, AnnualRegistryFormData } from "@/schemas/anualRegistrySchema";
import { toast } from "react-toastify";
import { Loader2, Upload, FileText, ExternalLink, RefreshCw } from "lucide-react";
import { DisorderMultiSelect } from "@/components/DisorderMultiSelect"; 

interface DocumentDTO {
    id: string;
    name: string;
    category: string;
    type: string;
    url: string;
}

interface AnnualRegistryEditModalProps {
    isOpen: boolean;
    onClose: () => void;
    patientId: string;
    currentYear: string;
    initialData: any; 
}

const MEDICAL_DOC_TYPES = [
    { value: "MEDICAL_REPORT", label: "Laudo Médico" },
    { value: "EXAMINATION", label: "Exame" },
    { value: "REFERRAL", label: "Encaminhamento" },
    { value: "OTHER", label: "Outro" }
];

export default function AnnualRegistryEditModal({ isOpen, onClose, patientId, currentYear, initialData }: AnnualRegistryEditModalProps) {
    
    // --- ESTADOS ---
    const [documents, setDocuments] = useState<DocumentDTO[]>([]);
    const [isLoadingDocs, setIsLoadingDocs] = useState(false);
    const [isUploading, setIsUploading] = useState(false);
    const [docType, setDocType] = useState("MEDICAL_REPORT");
    
    const [fullPatientData, setFullPatientData] = useState<any>(null);
    const fileInputRef = useRef<HTMLInputElement>(null);

    // --- FORMULÁRIO ---
    const form = useForm<AnnualRegistryFormData>({
        resolver: zodResolver(AnnualRegistryFormSchema),
        defaultValues: {
            bpc: "",
            familyIncome: "",
            diseases: "",
            continuousMedication: "",
            disorders: [],
            allergies: "",
            vaccines: ""
        },
    });

    // --- CARREGAMENTO ---
    useEffect(() => {
        if (isOpen && patientId) {
            fetchDocuments();
            fetchPatientData(); 
        }
    }, [isOpen, patientId]);

    useEffect(() => {
        if (isOpen) {
            let bpcValue = "Não";
            const rawBpc = initialData?.bpc;
            if (rawBpc === true || rawBpc === "true" || rawBpc === "Sim") {
                bpcValue = "Sim";
            } else if (rawBpc === "false" || rawBpc === false || rawBpc === "Não") {
                bpcValue = "Não";
            } else {
                bpcValue = "";
            }

            const vaccineStr = fullPatientData?.vaccineNames 
                ? fullPatientData.vaccineNames.map((v: any) => v.name).join(", ") 
                : (fullPatientData?.vaccines || "");

            form.reset({
                bpc: bpcValue,
                familyIncome: initialData?.familyIncome ? formatCurrencyForDisplay(initialData.familyIncome) : "",
                diseases: initialData?.diseases ?? "",
                continuousMedication: initialData?.continuousMedication ?? "",
                disorders: initialData?.disorders ?? [], 
                allergies: fullPatientData?.allergies ?? "",
                vaccines: vaccineStr
            });
        }
    }, [initialData, fullPatientData, isOpen, form]);

    // --- API CALLS ---
    const fetchDocuments = async () => {
        setIsLoadingDocs(true);
        try {
            const response = await fetch(`/api/pessoas/${patientId}/documentos/medicos`);
            if (response.ok) {
                const data = await response.json();
                setDocuments(data);
            }
        } catch (error) { console.error(error); } finally { setIsLoadingDocs(false); }
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
        } catch (error) { 
            toast.error("Erro ao enviar."); 
        } finally { 
            setIsUploading(false); 
        }
    };

    // --- HELPERS ---
    const cleanCurrency = (value: string) => {
        if (!value) return "0.00";
        const clean = value.replace(/[^\d,]/g, ''); 
        return clean.replace(',', '.');
    };
    
    const formatCurrencyForDisplay = (value: number | string) => {
        if (!value) return "";
        return Number(value).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
    };

    const handleMoneyChange = (e: React.ChangeEvent<HTMLInputElement>, onChange: (value: string) => void) => {
        let value = e.target.value.replace(/\D/g, "");
        if (value === "") { onChange(""); return; }
        onChange((parseFloat(value) / 100).toLocaleString("pt-BR", { style: "currency", currency: "BRL" }));
    };

    // --- SUBMIT ---
    const onSubmit = async (data: AnnualRegistryFormData) => {
        const registryId = initialData?.id; 
        
        try {
            // PASSO 0: GARANTIR QUE OS TRANSTORNOS EXISTEM (CRIAÇÃO AUTOMÁTICA)
            // Isso resolve o erro 404/500 quando o transtorno é novo.
            if (data.disorders && data.disorders.length > 0) {
                await Promise.all(data.disorders.map(async (d) => {
                    try {
                        // Tenta criar o transtorno. Se já existir, a API retorna erro (409) ou o objeto.
                        // Em ambos os casos, garantimos que ele existe no banco para o passo seguinte.
                        await fetch("/api/transtornos", {
                            method: "POST",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify({ name: d.name })
                        });
                    } catch (e) {
                        // Ignora erros aqui (como duplicidade), pois o objetivo é apenas garantir existência
                        console.warn(`Transtorno ${d.name} já existe ou houve erro na criação prévia.`);
                    }
                }));
            }

            // PASSO 1: REGISTRO ANUAL
            if (registryId) {
                const income = parseFloat(cleanCurrency(data.familyIncome));
                const regPayload = {
                    bpc: data.bpc === "Sim" ? "true" : "false", 
                    familyIncome: income,
                    diseases: data.diseases,
                    continuousMedication: data.continuousMedication,
                    disorders: data.disorders.map(d => ({ name: d.name })) 
                };

                const regRes = await fetch(`/api/pessoas/${patientId}/registro-anual/${registryId}`, {
                    method: 'PUT',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(regPayload),
                });

                if (!regRes.ok) {
                    const err = await regRes.json();
                    throw new Error(err.message || "Erro ao atualizar registro anual.");
                }
            }

            // PASSO 2: PACIENTE
            if (fullPatientData) {
                const vaccineList = data.vaccines.split(',')
                    .map(v => v.trim())
                    .filter(v => v !== "")
                    .map(v => ({ name: v }));

                const patientPayload = {
                    fullName: fullPatientData.fullName,
                    nationality: fullPatientData.birthplace, // Mapeamento correto
                    birthDate: fullPatientData.birthDate,
                    contact: fullPatientData.contact,
                    birthCertificateNumber: fullPatientData.birthCertificateNumber,
                    registryOffice: fullPatientData.registryOffice,
                    fls: fullPatientData.fls,
                    book: fullPatientData.book,
                    rg: fullPatientData.rg,
                    issueDate: fullPatientData.issueDate,
                    issuingAgency: fullPatientData.issuingAgency,
                    cpf: fullPatientData.cpf,
                    cns: fullPatientData.cns,
                    nis: fullPatientData.nis,
                    registrationDate: fullPatientData.registrationDate,
                    isStudent: fullPatientData.isStudent,
                    
                    allergies: data.allergies,
                    vaccineNames: vaccineList,

                    address: fullPatientData.address,
                    guardian: fullPatientData.guardian,
                    parents: fullPatientData.parents?.map((p: any) => ({
                        name: p.name,
                        rg: p.rg,
                        cpf: p.cpf,
                        profession: p.profession,
                        isAlive: p.isAlive,
                        kinship: p.kinship
                    })) ?? []
                };

                const patRes = await fetch(`/api/pessoas/${patientId}`, {
                    method: 'PUT', 
                    headers: { 'Content-Type': 'application/json' }, 
                    body: JSON.stringify(patientPayload)
                });

                if (!patRes.ok) {
                    const err = await patRes.json();
                    throw new Error(err.message || "Erro ao atualizar dados do paciente.");
                }
            }

            toast.success("Alterações salvas com sucesso!");
            onClose();
            window.location.reload(); 

        } catch (error: any) {
            console.error("Erro no submit:", error);
            toast.error(error.message || "Ocorreu um erro ao salvar.");
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="max-w-[1100px] h-[90vh] flex flex-col p-0 overflow-hidden bg-slate-50">
                <DialogHeader className="px-6 py-4 bg-white border-b shrink-0">
                    <DialogTitle className="text-[#0D4F97] text-xl font-bold">Editar Informações de Saúde ({currentYear})</DialogTitle>
                </DialogHeader>
                
                <div className="flex-1 overflow-y-auto p-6">
                    <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 h-full">
                        
                        {/* COLUNA ESQUERDA: FORMULÁRIO */}
                        <div className="bg-white p-6 rounded-xl shadow-sm border border-slate-200 h-fit">
                            <h3 className="text-[#0D4F97] font-bold mb-6 pb-2 border-b border-slate-100 flex items-center gap-2">
                                <span className="bg-blue-100 p-1 rounded">📋</span> Dados Clínicos
                            </h3>
                            
                            <Form {...form}>
                                <form id="health-form" onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
                                    <div className="grid grid-cols-2 gap-4">
                                        <FormField control={form.control} name="bpc" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-semibold">Recebe BPC?</FormLabel>
                                                <Select onValueChange={field.onChange} value={field.value}>
                                                    <FormControl><SelectTrigger className="bg-white"><SelectValue placeholder="Selecione" /></SelectTrigger></FormControl>
                                                    <SelectContent>
                                                        <SelectItem value="Sim">Sim</SelectItem>
                                                        <SelectItem value="Não">Não</SelectItem>
                                                    </SelectContent>
                                                </Select>
                                                <FormMessage />
                                            </FormItem>
                                        )} />
                                        
                                        <FormField control={form.control} name="familyIncome" render={({ field }) => (
                                            <FormItem>
                                                <FormLabel className="text-slate-700 font-semibold">Renda Familiar</FormLabel>
                                                <FormControl>
                                                    <Input className="bg-white" placeholder="R$ 0,00" value={field.value} onChange={(e) => handleMoneyChange(e, field.onChange)} />
                                                </FormControl>
                                                <FormMessage />
                                            </FormItem>
                                        )} />
                                    </div>

                                    <FormField control={form.control} name="diseases" render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-slate-700 font-semibold">Doenças (Histórico)</FormLabel>
                                            <FormControl><Input className="bg-white" {...field} /></FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )} />

                                    <FormField control={form.control} name="allergies" render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-slate-700 font-semibold">Alergias</FormLabel>
                                            <FormControl><Input className="bg-white" {...field} placeholder="Ex: Dipirona, Corantes..." /></FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )} />

                                    <FormField control={form.control} name="continuousMedication" render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-slate-700 font-semibold">Medicamentos Contínuos</FormLabel>
                                            <FormControl><Input className="bg-white" {...field} /></FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )} />

                                    <FormField control={form.control} name="vaccines" render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-slate-700 font-semibold">Vacinas</FormLabel>
                                            <FormControl>
                                                <Input className="bg-white" {...field} placeholder="Ex: Sarampo, Covid (Separe por vírgula)" />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )} />

                                    <FormField control={form.control} name="disorders" render={({ field }) => (
                                        <FormItem>
                                            <FormLabel className="text-slate-700 font-semibold">Transtornos/Deficiências</FormLabel>
                                            <FormControl>
                                                <DisorderMultiSelect 
                                                    value={field.value} 
                                                    onChange={field.onChange} 
                                                />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )} />
                                </form>
                            </Form>
                        </div>

                        {/* COLUNA DIREITA: DOCUMENTAÇÃO */}
                        <div className="flex flex-col h-full bg-white p-6 rounded-xl shadow-sm border border-slate-200">
                             <h3 className="text-[#0D4F97] font-bold mb-6 pb-2 border-b border-slate-100 flex items-center justify-between">
                                <span className="flex items-center gap-2"><span className="bg-green-100 p-1 rounded">📁</span> Documentação</span>
                                <Button variant="ghost" size="sm" className="h-8 w-8 p-0" onClick={fetchDocuments} disabled={isLoadingDocs}>
                                    <RefreshCw className={`h-4 w-4 ${isLoadingDocs ? 'animate-spin' : ''}`} />
                                </Button>
                            </h3>

                            <div className="flex-1 flex flex-col">
                                <div className="bg-slate-50 p-5 rounded-lg border-2 border-dashed border-slate-300 mb-6 hover:border-[#0D4F97]/50 transition-colors">
                                    <label className="text-xs font-bold text-slate-500 uppercase mb-3 block text-center">Adicionar Novo Documento</label>
                                    <div className="flex flex-col gap-3">
                                        <Select value={docType} onValueChange={setDocType}>
                                            <SelectTrigger className="w-full bg-white shadow-sm border-slate-200"><SelectValue /></SelectTrigger>
                                            <SelectContent>
                                                {MEDICAL_DOC_TYPES.map(t => <SelectItem key={t.value} value={t.value}>{t.label}</SelectItem>)}
                                            </SelectContent>
                                        </Select>
                                        
                                        <input type="file" ref={fileInputRef} onChange={handleFileUpload} className="hidden" accept=".pdf,.jpg,.png,.jpeg" disabled={isUploading} />
                                        
                                        <Button variant="outline" className="w-full bg-white text-[#0D4F97] border-[#0D4F97]/30 hover:bg-blue-50 hover:text-[#0b427d]" onClick={() => fileInputRef.current?.click()} disabled={isUploading}>
                                            {isUploading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : <Upload className="mr-2 h-4 w-4"/>} 
                                            Selecionar Arquivo
                                        </Button>
                                    </div>
                                </div>

                                <div className="flex-1 overflow-y-auto max-h-[400px] space-y-2 pr-2 custom-scrollbar">
                                    <h4 className="text-xs font-bold text-slate-500 uppercase mb-3">Arquivos Anexados ({documents.length})</h4>
                                    {documents.length === 0 ? (
                                        <div className="flex flex-col items-center justify-center h-32 text-slate-400 border rounded-lg bg-slate-50 border-slate-100">
                                            <FileText className="h-8 w-8 mb-2 opacity-20" />
                                            <p className="text-sm italic">Nenhum documento.</p>
                                        </div>
                                    ) : (
                                        documents.map((doc) => (
                                            <div key={doc.id} className="group flex items-center justify-between p-3 bg-white border border-gray-100 rounded-lg shadow-sm hover:shadow-md hover:border-[#0D4F97]/30 transition-all">
                                                <div className="flex items-center gap-3 overflow-hidden">
                                                    <div className="bg-blue-50 p-2 rounded-lg group-hover:bg-blue-100 transition-colors">
                                                        <FileText className="h-5 w-5 text-[#0D4F97]" />
                                                    </div>
                                                    <div className="flex flex-col min-w-0">
                                                        <span className="text-sm font-medium truncate text-slate-700" title={doc.name}>{doc.name}</span>
                                                        <span className="text-[10px] text-slate-400 font-semibold">{doc.type}</span>
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
                        </div>
                    </div>
                </div>

                <DialogFooter className="px-6 py-4 bg-white border-t shrink-0 flex justify-end gap-3">
                    <Button variant="ghost" onClick={onClose} type="button" className="text-slate-500 hover:text-slate-800">Cancelar</Button>
                    <Button form="health-form" type="submit" className="text-white bg-[#0D4F97] hover:bg-[#0b427d] shadow-md px-6">
                        Salvar Alterações
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    );
}
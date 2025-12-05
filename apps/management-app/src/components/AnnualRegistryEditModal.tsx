"use client";

import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { AnnualRegistryFormSchema, AnnualRegistryFormData } from "@/schemas/anualRegistrySchema"; 
import { toast } from "react-toastify"; 

interface AnnualRegistryEditModalProps {
    isOpen: boolean;
    onClose: () => void;
    patientId: string;
    currentYear: string;
    initialData: any; 
}

export default function AnnualRegistryEditModal({ isOpen, onClose, patientId, currentYear, initialData }: AnnualRegistryEditModalProps) {
    
    const cleanAndFormatCurrency = (value: string): string => {
        if (!value) return "0.00";
        return value.replace(/R\$/, '').replace(/\./g, '').replace(',', '.').trim();
    };

    const formatCurrencyForInput = (value: number | string) => {
        return value ? String(value) : ""; 
    };
    
    const formatDisordersForInput = (disorders: any[] | undefined): string => {
        return disorders?.map(d => d.name)?.join(', ') ?? '';
    }

    const form = useForm<AnnualRegistryFormData>({
        resolver: zodResolver(AnnualRegistryFormSchema),
        defaultValues: {
            bpc: initialData?.bpc === 'Sim' || initialData?.bpc === true, 
            familyIncome: formatCurrencyForInput(initialData?.familyIncome),
            diseases: initialData?.diseases ?? "",
            continuousMedication: initialData?.continuousMedication ?? "",
            disorders: formatDisordersForInput(initialData?.disorders),
        },
    });

    const onSubmit = async (data: AnnualRegistryFormData) => {
        const registryId = initialData?.id;

        if (!registryId) {
            toast.error("Erro: ID do registro anual ausente. Não é possível atualizar.");
            return;
        }

        const formattedDisorders = data.disorders
            ? data.disorders.split(',').map(name => ({ name: name.trim() })) 
            : [];
            
        const payload = {
            bpc: data.bpc ? "true" : "false", 
            familyIncome: cleanAndFormatCurrency(data.familyIncome), 
            diseases: data.diseases,
            
            continuousMedication: data.continuousMedication,
            disorders: formattedDisorders
        };

        try {
            const response = await fetch(`/api/pessoas/${patientId}/registro-anual/${registryId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Falha ao salvar registro.");
            }

            toast.success("Registro anual atualizado com sucesso!");
            onClose();
            window.location.reload(); 

        } catch (error: any) {
            console.error("Erro na submissão:", error);
            toast.error(error.message || "Erro ao salvar alterações.");
        }
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[600px]">
                <DialogHeader>
                    <DialogTitle className="text-[#0D4F97]" >Editar Registro Anual ({currentYear})</DialogTitle>
                </DialogHeader>
                
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
                        
                        {/* Campo 'Recebe BPC' */}
                        <FormField
                            control={form.control}
                            name="bpc"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-[#0D4F97]" >Recebe BPC?</FormLabel>
                                    <Select 
                                        onValueChange={(value) => field.onChange(value === 'true')} 
                                        defaultValue={String(field.value)}
                                    >
                                        <FormControl>
                                            <SelectTrigger>
                                                <SelectValue placeholder="Selecione" />
                                            </SelectTrigger>
                                        </FormControl>
                                        <SelectContent>
                                            <SelectItem value="true">Sim</SelectItem>
                                            <SelectItem value="false">Não</SelectItem>
                                        </SelectContent>
                                    </Select>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        
                        {/* Campo Renda Familiar */}
                        <FormField
                            control={form.control}
                            name="familyIncome"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-[#0D4F97]">Renda Familiar *</FormLabel>
                                    <FormControl>
                                        <Input {...field} placeholder="R$ 0,00" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Campo Doenças */}
                        <FormField
                            control={form.control}
                            name="diseases"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-[#0D4F97]">Doenças (Histórico)</FormLabel>
                                    <FormControl>
                                        <Input {...field} placeholder="Descreva doenças crônicas ou históricas" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Campo Medicamentos Contínuos */}
                        <FormField
                            control={form.control}
                            name="continuousMedication"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-[#0D4F97]" >Medicamentos Contínuos</FormLabel>
                                    <FormControl>
                                        <Input {...field} placeholder="Liste os medicamentos de uso contínuo" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />

                        {/* Campo Transtornos */}
                        <FormField
                            control={form.control}
                            name="disorders"
                            render={({ field }) => (
                                <FormItem>
                                    <FormLabel className="text-[#0D4F97]" >Transtornos/Deficiências</FormLabel>
                                    <FormControl>
                                        <Input {...field} placeholder="TDAH, Autismo, etc. (Separe por vírgulas)" />
                                    </FormControl>
                                    <FormMessage />
                                </FormItem>
                            )}
                        />
                        
                        <DialogFooter className="pt-6">
                            <Button className="text-[#0D4F97]" variant="outline" onClick={onClose} type="button">
                                Cancelar
                            </Button>
                            <Button type="submit" className="text-white !bg-[#0D4F97] !hover:bg-[#0b427d]">
                                Salvar Alterações
                            </Button>
                        </DialogFooter>
                    </form>
                </Form>
            </DialogContent>
        </Dialog>
    );
}
import React from 'react';
import { PessoaRequest } from '../service/pessoaService';
import { Label } from '@radix-ui/react-label';
import { Input } from '@/components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    addFile: (key: string, file: File, category: string, type: string) => void;
    nextStep: () => void;
}

export default function CadastroUm({ data, setData, addFile, nextStep }: Props) {
    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setData(prev => ({ ...prev, [name]: value }));
    };

    const handleContactChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value } = e.target;
        setData(prev => ({
            ...prev,
            contatoRequest: {
                ...prev.contatoRequest,
                [name]: value
            }
        }));
    };

    return (
        <Card className="w-full max-w-6xl mx-auto">
            <CardHeader>
                <CardTitle>Passo 1: Dados Pessoais e Endereço</CardTitle>
                <CardDescription>Preencha as informações do assistido.</CardDescription>
            </CardHeader>
            <CardContent>
                {/* Usamos grid layout do Tailwind para organizar os campos */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">

                    {/* Seção de Dados Pessoais */}
                    <div className="space-y-4">
                        <h3 className="text-lg font-medium">Dados Pessoais</h3>
                        
                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="nomeCompleto">Nome Completo</Label>
                            <Input type="text" id="nomeCompleto" name="nomeCompleto" value={data.nomeCompleto} onChange={handleChange} />
                        </div>
                        
                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="dataNascimento">Data de Nascimento</Label>
                            <Input type="date" id="dataNascimento" name="dataNascimento" value={data.dataNascimento} onChange={handleChange} />
                        </div>
                        
                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="cpf">CPF</Label>
                            <Input type="text" id="cpf" name="cpf" value={data.cpf} onChange={handleChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="numeroTelefone">Contato (Telefone)</Label>
                            <Input type="tel" id="numeroTelefone" name="numeroTelefone" value={data.contatoRequest.numeroTelefone} onChange={handleContactChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="rg">RG</Label>
                            <Input type="text" id="rg" name="rg" value={data.rg} onChange={handleChange} />
                        </div>
                        
                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="dataEmissaoRg">Data de Emissão do RG</Label>
                            <Input type="date" id="dataEmissaoRg" name="dataEmissaoRg" value={data.dataEmissaoRg} onChange={handleChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="orgaoEmissorRg">Órgão Emissor do RG</Label>
                            <Input type="text" id="orgaoEmissorRg" name="orgaoEmissorRg" value={data.orgaoEmissorRg} onChange={handleChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="numRegistroNasc">Nº do Registro de Nascimento</Label>
                            <Input type="text" id="numRegistroNasc" name="numRegistroNasc" value={data.numRegistroNasc} onChange={handleChange} />
                        </div>
                    </div>

                    {/* Seção de Endereço */}
                    <div className="space-y-4">
                        <h3 className="text-lg font-medium">Endereço</h3>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="cep">CEP</Label>
                            <Input type="text" id="cep" name="cep" value={data.contatoRequest.cep} onChange={handleContactChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="bairro">Bairro</Label>
                            <Input type="text" id="bairro" name="bairro" value={data.contatoRequest.bairro} onChange={handleContactChange} />
                        </div>

                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="cidade">Cidade</Label>
                            <Input type="text" id="cidade" name="cidade" value={data.contatoRequest.cidade} onChange={handleContactChange} />
                        </div>
                        
                        <div className="grid w-full items-center gap-1.5">
                            <Label htmlFor="estado">Estado</Label>
                            <Input type="text" id="estado" name="estado" value={data.contatoRequest.estado} onChange={handleContactChange} />
                        </div>
                    </div>
                </div>

                {/* Botão de Ação */}
                <div className="flex justify-end mt-8">
                    <Button onClick={nextStep}>Próximo</Button>
                </div>
            </CardContent>
        </Card>
    );
}
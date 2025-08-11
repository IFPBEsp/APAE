// components/CadastroUm.tsx
import React from 'react';
import { PessoaRequest } from '../service/pessoaService';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    addFile: (key: string, file: File) => void;
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
    
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files[0]) {
            addFile('comprovanteResidencia', e.target.files[0]);
            setData(prev => ({
                ...prev,
                contatoRequest: {
                    ...prev.contatoRequest,
                    comprovanteResidencia: e.target.files?.[0].name || ''
                }
            }));
        }
    };

    return (
        <div>
            <h2>Passo 1: Dados Pessoais e Endereço</h2>
            <h3>Dados Pessoais</h3>
            <label>Nome Completo: <input name="nomeCompleto" value={data.nomeCompleto} onChange={handleChange} /></label><br />
            <label>Data de Nascimento: <input name="dataNascimento" type="date" value={data.dataNascimento} onChange={handleChange} /></label><br />
            <label>CPF: <input name="cpf" value={data.cpf} onChange={handleChange} /></label><br />
            <label>RG: <input name="rg" value={data.rg} onChange={handleChange} /></label><br />
            <h3>Endereço</h3>
            <label>CEP: <input name="cep" value={data.contatoRequest.cep} onChange={handleContactChange} /></label><br />
            <label>Endereço: <input name="endereco" value={data.contatoRequest.endereco} onChange={handleContactChange} /></label><br />
            <label>Bairro: <input name="bairro" value={data.contatoRequest.bairro} onChange={handleContactChange} /></label><br />
            <label>Cidade: <input name="cidade" value={data.contatoRequest.cidade} onChange={handleContactChange} /></label><br />
            <label>Estado: <input name="estado" value={data.contatoRequest.estado} onChange={handleContactChange} /></label><br />
            <label>Comprovante de Residência: <input type="file" onChange={handleFileChange} /></label><br />

            <hr />
            <button onClick={nextStep}>Próximo</button>
        </div>
    );
}
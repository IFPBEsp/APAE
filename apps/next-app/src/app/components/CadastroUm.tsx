import React from 'react';
import { PessoaRequest } from '../service/pessoaService';

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
        <div>
            <h2>Passo 1: Dados Pessoais e Endereço</h2>
            
            <h3>Dados Pessoais</h3>
            <label>Nome Completo: <input name="nomeCompleto" value={data.nomeCompleto} onChange={handleChange} /></label><br />
            <label>Data de Nascimento: <input name="dataNascimento" type="date" value={data.dataNascimento} onChange={handleChange} /></label><br />
            <label>CPF: <input name="cpf" value={data.cpf} onChange={handleChange} /></label><br />
            <label>Contato: <input name="numeroTelefone" value={data.numeroTelefone} onChange={handleChange} /></label><br />
            <label>RG: <input name="rg" value={data.rg} onChange={handleChange} /></label><br />
            <label>Data de Emissão do RG: <input name="dataEmissaoRg" type="date" value={data.dataEmissaoRg} onChange={handleChange} /></label><br />
            <label>Órgão Emissor do RG: <input name="orgaoEmissorRg" value={data.orgaoEmissorRg} onChange={handleChange} /></label><br />
            <label>Número do Registro de Nascimento: <input name="numRegistroNasc" value={data.numRegistroNasc} onChange={handleChange} /></label><br />

            <h3>Endereço</h3>
            <label>CEP: <input name="cep" value={data.contatoRequest.cep} onChange={handleContactChange} /></label><br />
            <label>Bairro: <input name="bairro" value={data.contatoRequest.bairro} onChange={handleContactChange} /></label><br />
            <label>Cidade: <input name="cidade" value={data.contatoRequest.cidade} onChange={handleContactChange} /></label><br />
            <label>Estado: <input name="estado" value={data.contatoRequest.estado} onChange={handleContactChange} /></label><br />

            <button onClick={nextStep}>Próximo</button>
        </div>
    );
}

import React from 'react';
import { PessoaRequest, PessoaResponsavelRequest } from '../service/pessoaService';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    nextStep: () => void;
    prevStep: () => void;
}

export default function CadastroTres({ data, setData, nextStep, prevStep }: Props) {
    const handleAddResponsavel = () => {
        setData(prev => ({
            ...prev,
            responsaveisRequests: [...prev.responsaveisRequests, {
                nome: '', ondeProcurar: '', vivo: true, profissao: '', rg: '', cpf: '', emergencia: '', tipoResponsavel: ''
            }]
        }));
    };

    const handleResponsavelChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        const updatedResponsaveis = [...data.responsaveisRequests];
        updatedResponsaveis[index] = { ...updatedResponsaveis[index], [name]: value };
        setData(prev => ({ ...prev, responsaveisRequests: updatedResponsaveis }));
    };

    return (
        <div>
            <h2>Passo 3: Responsáveis</h2>
            {data.responsaveisRequests.map((resp, index) => (
                <div key={index} style={{ border: '1px solid #ccc', padding: '10px', marginBottom: '10px' }}>
                    <h4>Responsável {index + 1}</h4>
                    <label>Nome: <input name="nome" value={resp.nome} onChange={(e) => handleResponsavelChange(index, e)} /></label><br />
                    <label>CPF: <input name="cpf" value={resp.cpf} onChange={(e) => handleResponsavelChange(index, e)} /></label><br />
                    <label>RG: <input name="rg" value={resp.rg} onChange={(e) => handleResponsavelChange(index, e)} /></label><br />
                </div>
            ))}
            <button onClick={handleAddResponsavel}>Adicionar Responsável</button>
            <hr />
            <button onClick={prevStep}>Anterior</button>
            <button onClick={nextStep}>Próximo</button>
        </div>
    );
}
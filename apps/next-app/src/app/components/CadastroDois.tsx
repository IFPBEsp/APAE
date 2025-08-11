import React from 'react';
import { PessoaRequest, VacinaRequest, TipoDeficienciaRequest } from '../service/pessoaService';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    addFile: (key: string, file: File) => void;
    nextStep: () => void;
    prevStep: () => void;
}

export default function CadastroDois({ data, setData, addFile, nextStep, prevStep }: Props) {
    const handleAddVacina = () => {
        setData(prev => ({
            ...prev,
            vacinacoesRequests: [...prev.vacinacoesRequests, { nome: '', dataAplicacao: '' }]
        }));
    };
    
    const handleAddDeficiencia = () => {
        setData(prev => ({
            ...prev,
            deficienciasRequests: [...prev.deficienciasRequests, { descricao: '' }]
        }));
    };
    
    const handleVacinaChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        const updatedVacinas = [...data.vacinacoesRequests];
        updatedVacinas[index] = { ...updatedVacinas[index], [name]: value };
        setData(prev => ({ ...prev, vacinacoesRequests: updatedVacinas }));
    };

    return (
        <div>
            <h2>Passo 2: Saúde, Laudos e Deficiências</h2>
            
            <h3>Vacinas</h3>
            {data.vacinacoesRequests.map((vacina, index) => (
                <div key={index}>
                    <label>Nome da Vacina: <input name="nome" value={vacina.nome} onChange={(e) => handleVacinaChange(index, e)} /></label>
                    <label>Data de Aplicação: <input name="dataAplicacao" type="date" value={vacina.dataAplicacao} onChange={(e) => handleVacinaChange(index, e)} /></label>
                </div>
            ))}
            <button onClick={handleAddVacina}>Adicionar Vacina</button>
            <br /><br />

            <h3>Deficiências</h3>
            <button onClick={handleAddDeficiencia}>Adicionar Deficiência</button>
            <br /><br />
            
            <h3>Laudos e Documentos Médicos</h3>
            <p>Faça o upload dos laudos aqui.</p>
            <label>Laudo 1: <input type="file" onChange={(e) => e.target.files && addFile('laudo_1', e.target.files[0])} /></label><br/>
            <label>Laudo 2: <input type="file" onChange={(e) => e.target.files && addFile('laudo_2', e.target.files[0])} /></label><br/>
            
            <hr />
            <button onClick={prevStep}>Anterior</button>
            <button onClick={nextStep}>Próximo</button>
        </div>
    );
}

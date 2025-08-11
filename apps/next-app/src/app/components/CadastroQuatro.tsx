import React from 'react';
import { PessoaRequest } from '../service/pessoaService';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    handleSubmit: () => void;
    prevStep: () => void;
}

export default function CadastroQuatro({ data, setData, handleSubmit, prevStep }: Props) {

    const handleCadastroAnualChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
        const { name, value, type } = e.target;
        
        let val: string | number | boolean = value;
        if (type === 'number') val = parseFloat(value);
        if (name === 'beneficioDePrestacaoContinuada') val = (e.target as HTMLInputElement).checked;

        const updatedCadastros = [...(data.cadastrosAnuaisRequests || [])];
        if (updatedCadastros.length === 0) {
            updatedCadastros.push({
                beneficioDePrestacaoContinuada: false,
                historicosAlergias: '',
                medicacoesContinuas: '',
                historicoDoencas: '',
                rendaFamiliar: 0,
            });
        }
        (updatedCadastros[0] as any)[name] = val;

        setData(prev => ({ ...prev, cadastrosAnuaisRequests: updatedCadastros }));
    };
    
    const cadastroAnual = data.cadastrosAnuaisRequests?.[0] || {};

    return (
        <div>
            <h2>Passo 4: Informações Adicionais e Conclusão</h2>
            
            <h3>Cadastro Anual</h3>
            <label>
                Benefício de Prestação Continuada (BPC): 
                <input 
                    name="beneficioDePrestacaoContinuada" 
                    type="checkbox" 
                    checked={!!cadastroAnual.beneficioDePrestacaoContinuada} 
                    onChange={handleCadastroAnualChange} 
                />
            </label><br />
            <label>Renda Familiar (R$): <input name="rendaFamiliar" type="number" value={cadastroAnual.rendaFamiliar || ''} onChange={handleCadastroAnualChange} /></label><br />
            <label>Histórico de Alergias: <textarea name="historicosAlergias" value={cadastroAnual.historicosAlergias || ''} onChange={handleCadastroAnualChange}></textarea></label><br />
            
            <p>
                Ao clicar em "Enviar Cadastro", você confirma que todas as informações
                fornecidas estão corretas.
            </p>
            
            <hr />
            <button onClick={prevStep}>Anterior</button>
            <button onClick={handleSubmit} style={{ fontWeight: 'bold' }}>
                Enviar Cadastro
            </button>
        </div>
    );
}
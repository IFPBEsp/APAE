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
    const { name, value, type, checked } = e.target;
    const updatedResponsaveis = [...data.responsaveisRequests];
    updatedResponsaveis[index] = { ...updatedResponsaveis[index], [name]: type === 'checkbox' ? checked : value };
    setData(prev => ({ ...prev, responsaveisRequests: updatedResponsaveis }));
  };

  const handleRendaFamiliarChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    setData(prev => {
      const updatedCadastros = [...(prev.cadastrosAnuaisRequests || [])];
      if (updatedCadastros.length === 0) {
        updatedCadastros.push({
          beneficioDePrestacaoContinuada: false,
          historicosAlergias: '',
          medicacoesContinuas: '',
          historicoDoencas: '',
          rendaFamiliar: 0,
        });
      }
      updatedCadastros[0].rendaFamiliar = parseFloat(value) || 0;
      return { ...prev, cadastrosAnuaisRequests: updatedCadastros };
    });
  };

  const rendaFamiliar = data.cadastrosAnuaisRequests?.[0]?.rendaFamiliar || 0;

  return (
    <div>
      <h2>Passo 3: Responsáveis e Renda Familiar</h2>
      <hr />
      <div>
        <label>
          <span>Renda Familiar (R$):</span>
          <input
            name="rendaFamiliar"
            type="number"
            value={rendaFamiliar}
            onChange={handleRendaFamiliarChange}
          />
        </label>
      </div>

      <h3>Dados dos Responsáveis</h3>
      <div>
        {data.responsaveisRequests.map((resp, index) => (
          <div key={index}>
            <h4>Responsável {index + 1}</h4>
            <div>
              <label>
                <span>Nome:</span>
                <input name="nome" value={resp.nome} onChange={(e) => handleResponsavelChange(index, e)} />
              </label>
              <label>
                <span>CPF:</span>
                <input name="cpf" value={resp.cpf} onChange={(e) => handleResponsavelChange(index, e)} />
              </label>
              <label>
                <span>RG:</span>
                <input name="rg" value={resp.rg} onChange={(e) => handleResponsavelChange(index, e)} />
              </label>
              <label>
                <span>Profissão:</span>
                <input name="profissao" value={resp.profissao} onChange={(e) => handleResponsavelChange(index, e)} />
              </label>
              <label>
                <input
                  type="checkbox"
                  name="vivo"
                  checked={resp.vivo}
                  onChange={(e) => handleResponsavelChange(index, e)}
                />
                <span>Vivo</span>
              </label>
            </div>
          </div>
        ))}
        <button onClick={handleAddResponsavel}>Adicionar Responsável</button>
      </div>

      <div>
        <button onClick={prevStep}>Anterior</button>
        <button onClick={nextStep}>Próximo</button>
      </div>
    </div>
  );
}
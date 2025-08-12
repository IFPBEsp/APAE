import React from 'react';
import { PessoaRequest } from '../service/pessoaService';

interface Props {
    data: PessoaRequest;
    setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
    addFile: (key: string, file: File, category: string, type: string) => void;
    handleSubmit: () => void;
    prevStep: () => void;
}

export default function CadastroQuatro({ data, setData, addFile, handleSubmit, prevStep }: Props) {
  const handleFuncaoChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setData(prev => ({ ...prev, funcao: e.target.value as 'Aluno' | 'Paciente' | 'Ambos' }));
  };

  const handleDataCadastroChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setData(prev => ({ ...prev, dataCadastramento: e.target.value }));
  };

  return (
    <div>
      <h2>Passo 4: Informações Adicionais e Conclusão</h2>
      <hr />
      
      <div>
        <label>
          <span>Data de Cadastro:</span>
          <input
            name="dataCadastramento"
            type="date"
            value={data.dataCadastramento}
            onChange={handleDataCadastroChange}
          />
        </label>
        
        <label>
          <span>Adicionar uma Foto:</span>
          <input 
            type="file" 
            onChange={(e) => e.target.files && addFile('foto', e.target.files[0], "PESSOAL", "FOTO")} 
          />
        </label>

        <label>
          <span>Selecionar Função:</span>
          <select 
            name="funcao" 
            value={data.funcao || ''} 
            onChange={handleFuncaoChange}
          >
            <option value="">Selecione...</option>
            <option value="Aluno">Aluno</option>
            <option value="Paciente">Paciente</option>
            <option value="Ambos">Ambos</option>
          </select>
        </label>
      </div>

      <div>
        <button onClick={prevStep}>Anterior</button>
        <button onClick={handleSubmit}>
          Enviar Cadastro
        </button>
      </div>
    </div>
  );
}
import React from 'react';
import { PessoaRequest } from '../service/pessoaService';

interface Props {
  data: PessoaRequest;
  setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
  addFile: (key: string, file: File, category: string, type: string) => void;
  nextStep: () => void;
  prevStep: () => void;
}

export default function CadastroDois({ data, setData, addFile, nextStep, prevStep }: Props) {
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setData(prev => ({
      ...prev,
      cadastrosAnuaisRequests: [{
        ...prev.cadastrosAnuaisRequests[0],
        [name]: value,
        beneficioDePrestacaoContinuada: false
      }],
    }));
  };

  const handleBeneficioChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { checked } = e.target;
    const updatedCadastros = data.cadastrosAnuaisRequests.length > 0
      ? data.cadastrosAnuaisRequests
      : [{
          beneficioDePrestacaoContinuada: false,
          historicosAlergias: '',
          medicacoesContinuas: '',
          historicoDoencas: '',
          rendaFamiliar: 0,
        }];

    setData(prev => ({
      ...prev,
      cadastrosAnuaisRequests: [{
        ...updatedCadastros[0],
        beneficioDePrestacaoContinuada: checked,
      }],
    }));
  };

  const handleAddVacina = () => {
    setData(prev => ({
      ...prev,
      vacinacoesRequests: [...prev.vacinacoesRequests, { nome: '', dataAplicacao: '' }],
    }));
  };

  const handleVacinaChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    const updatedVacinas = [...data.vacinacoesRequests];
    updatedVacinas[index] = { ...updatedVacinas[index], [name]: value };
    setData(prev => ({ ...prev, vacinacoesRequests: updatedVacinas }));
  };

  const handleAddDeficiencia = () => {
    setData(prev => ({
      ...prev,
      deficienciasRequests: [...prev.deficienciasRequests, { descricao: '' }],
    }));
  };

  const handleDeficienciaChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    const updatedDeficiencias = [...data.deficienciasRequests];
    updatedDeficiencias[index] = { descricao: value };
    setData(prev => ({ ...prev, deficienciasRequests: updatedDeficiencias }));
  };

  const handleAddAtendimento = () => {
    setData(prev => ({
      ...prev,
      atendimentosRequests: [...prev.atendimentosRequests, { descricao: '' }],
    }));
  };

  const handleAtendimentoChange = (index: number, e: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = e.target;
    const updatedAtendimentos = [...data.atendimentosRequests];
    updatedAtendimentos[index] = { descricao: value };
    setData(prev => ({ ...prev, atendimentosRequests: updatedAtendimentos }));
  };

  return (
    <div>
      <h2>Passo 2: Saúde, Alergias, Deficiências e Atendimentos</h2>

      <label>Doenças que já teve:<br />
        <textarea
          name="historicoDoencas"
          value={data.cadastrosAnuaisRequests[0]?.historicoDoencas || ''}
          onChange={handleChange}
          rows={3}
          cols={40}
        />
      </label>
      <br /><br />

      <label>Medicação contínua:<br />
        <textarea
          name="medicacoesContinuas"
          value={data.cadastrosAnuaisRequests[0]?.medicacoesContinuas || ''}
          onChange={handleChange}
          rows={3}
          cols={40}
        />
      </label>
      <br /><br />

      <label>Alergias:<br />
        <textarea
          name="historicosAlergias"
          value={data.cadastrosAnuaisRequests[0]?.historicosAlergias || ''}
          onChange={handleChange}
          rows={3}
          cols={40}
        />
      </label>
      <br /><br />

      <h3>Vacinas Tomadas</h3>
      {data.vacinacoesRequests.map((vacina, index) => (
        <div key={index}>
          <label>Nome da Vacina: <input name="nome" value={vacina.nome} onChange={(e) => handleVacinaChange(index, e)} /></label>
          <label>Data de Aplicação: <input name="dataAplicacao" type="date" value={vacina.dataAplicacao} onChange={(e) => handleVacinaChange(index, e)} /></label>
        </div>
      ))}
      <button type="button" onClick={handleAddVacina}>Adicionar Vacina</button>
      <br /><br />

      <h3>Tipos de Deficiência</h3>
      {data.deficienciasRequests.map((deficiencia, index) => (
        <div key={index}>
          <input
            value={deficiencia.descricao}
            onChange={(e) => handleDeficienciaChange(index, e)}
            placeholder="Descrição da deficiência"
          />
        </div>
      ))}
      <button type="button" onClick={handleAddDeficiencia}>Adicionar Deficiência</button>
      <br /><br />

      <h3>Tipos de Atendimento</h3>
      {data.atendimentosRequests.map((atendimento, index) => (
        <div key={index}>
          <input
            value={atendimento.descricao}
            onChange={(e) => handleAtendimentoChange(index, e)}
            placeholder="Descrição do atendimento"
          />
        </div>
      ))}
      <button type="button" onClick={handleAddAtendimento}>Adicionar Atendimento</button>
      <br /><br />

      <label>
        <input
          type="checkbox"
          name="bpc"
          checked={data.cadastrosAnuaisRequests[0].beneficioDePrestacaoContinuada}
          onChange={(e) => handleBeneficioChange(e)}
        />
        <span>Possui benefício de prestação continuada?</span>
      </label>

      <h3>Documentos</h3>
      <label>Enviar Laudo: <input type="file" onChange={(e) => e.target.files && addFile('laudo', e.target.files[0], "MEDICO", "LAUDO")} /></label><br />
      <label>Enviar Encaminhamento: <input type="file" onChange={(e) => e.target.files && addFile('encaminhamento', e.target.files[0], "MEDICO", "ENCAMINHAMENTO")} /></label><br />

      <button type="button" onClick={prevStep}>Anterior</button>
      <button type="button" onClick={nextStep}>Próximo</button>
    </div>
  );
}

"use client";

import React, { useState } from "react";
import styles from "./page.module.css";

const CadastroProfissional = () => {
  const [formData, setFormData] = useState({
    nomeCompleto: "",
    email: "",
    areaSaude: "",
    fiscalidade: "",
    documentoProfissional: "",
    cpf: "",
    telefone: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Dados salvos:", formData);
  };

  return (
    <div className={styles.appContainer}>
      <div className={styles.mainContent}>
        <header className={styles.header}>
          <h1>Atualizar Profissional</h1>
        </header>

        <form onSubmit={handleSubmit} className={styles.form}>
          <section className={styles.formSection}>
            <h2>Dados gerais</h2>

            <div className={styles.formGroup}>
              <label htmlFor="nomeCompleto">Nome completo</label>
              <input
                type="text"
                id="nomeCompleto"
                name="nomeCompleto"
                value={formData.nomeCompleto}
                onChange={handleChange}
                placeholder="Ex: Maria da Silva"
                required
              />
            </div>

            <div className={styles.formGroup}>
              <label htmlFor="email">Email</label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Ex: profissional@exemplo.com"
                required
              />
            </div>

            <div className={styles.rowContainer}>
              <div className={`${styles.formGroup} ${styles.rowItem}`}>
                <label htmlFor="documentoProfissional">
                  Documento profissional
                </label>
                <input
                  type="text"
                  id="documentoProfissional"
                  name="documentoProfissional"
                  value={formData.documentoProfissional}
                  onChange={handleChange}
                  placeholder="Ex: CRM/SP 123456"
                  required
                />
              </div>

              <div
                className={`${styles.formGroup} ${styles.rowItem} ${styles.areaSaudeGroup}`}
              >
                <label htmlFor="areaSaude">Área da saúde</label>
                <select
                  id="areaSaude"
                  name="areaSaude"
                  value={formData.areaSaude}
                  onChange={handleChange}
                  required
                >
                  <option value="">Selecione uma opção</option>
                  <option value="Medicina">Medicina</option>
                  <option value="Enfermagem">Enfermagem</option>
                  <option value="Fisioterapia">Fisioterapia</option>
                  <option value="Psicologia">Psicologia</option>
                  <option value="Nutrição">Nutrição</option>
                </select>
              </div>
            </div>

            <div className={styles.fieldsRow}>
              <div className={`${styles.formGroup} ${styles.fieldColumn}`}>
                <label htmlFor="cpf">CPF</label>
                <input
                  type="text"
                  id="cpf"
                  name="cpf"
                  value={formData.cpf}
                  onChange={handleChange}
                  placeholder="Ex: 123.456.789-00"
                  required
                />
              </div>

              <div className={`${styles.formGroup} ${styles.fieldColumn}`}>
                <label htmlFor="telefone">Telefone</label>
                <input
                  type="tel"
                  id="telefone"
                  name="telefone"
                  value={formData.telefone}
                  onChange={handleChange}
                  placeholder="Ex: (11) 98765-4321"
                  required
                />
              </div>
            </div>
          </section>

          <div className={styles.formActions}>
            <button type="button" className={styles.cancelButton}>
              Cancelar
            </button>
            <button type="submit" className={styles.saveButton}>
              Salvar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CadastroProfissional;

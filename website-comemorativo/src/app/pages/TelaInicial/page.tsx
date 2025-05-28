"use client";

import styles from "./page.module.css";
import Image from "next/image";
import Link from "next/link";
import LeitorDeTexto from '../../components/leitor/LeitorDeTexto'; 

export default function HomePage() {
  return (
    <>
      <div className={styles.hero}>
        <div className={styles.overlay}>
          <h1>Associação de Pais e Amigos dos <br /> Excepcionais de Esperança</h1>
          <Image
            src="/logo-30anos.png"
            alt="Logo 30 anos APAE"
            width={200}
            height={200}
          />
        </div>
      </div>

      <main className={styles.content}>
        <section className={styles.intro}>
          {/* Botão para ler o texto */}
          <LeitorDeTexto texto="Título principal: Associação de Pais e Amigos dos Excepcionais de Esperança.
            Subtítulo de primeiro nível: 30 Anos Fazendo a Diferença. 
            Subtítulo de segundo nível: APAE Esperança.

            A APAE (Associação de Pais e Amigos dos Excepcionais) de Esperança - PB é
            uma instituição filantrópica, sem fins lucrativos criada em outubro de
            1995, que tem a finalidade de prestar assistência integral às pessoas
            com deficiência intelectual. Para manter as suas atividades, a
            instituição oferece diversos serviços à comunidade que propiciam a sua
            viabilidade económica.
            Os recursos arrecadados com os serviços são revertidos para o atendimento
            a pessoas com deficiências.
            
            Sessão dos trinta anos.

            Subtítulo de terceiro nível: Conheça Mais Sobre a Nossa Trajetória.
            Na seção de 30 anos da APAE, celebramos e relembrando momentos marcantes
            por meio de uma linha do tempo, depoimentos emocionantes e eventos comemorativos.
            Mais do que uma celebração, essa data simboliza o compromisso contínuo com a
            inclusão, o respeito e a construção de uma sociedade mais justa para todos.
            Botão: 30 Anos da APAE

            Parceiros: Prefeitura de Esperança
            " />

          <h2>30 Anos Fazendo a Diferença</h2>
          <h3>APAE Esperança</h3>
          <p>
            A APAE (Associação de Pais e Amigos dos Excepcionais) de Esperança - PB</p> <p> é
            uma instituição filantrópica, sem fins lucrativos criada em outubro de
            1995, que tem a finalidade de prestar assistência integral às pessoas
            com deficiência intelectual. Para manter as suas atividades, a
            instituição oferece diversos serviços à comunidade que propiciam a sua
            viabilidade económica.
            Os recursos arrecadados com os serviços são revertidos para o atendimento
            a pessoas com deficiências.
          </p>

        </section>

        <section className={styles.ctaSection}>
          <h4>Conheça Mais Sobre a Nossa Trajetória</h4>
          <div className={styles.ctaBox}>
            <p>
              Na seção de 30 anos da APAE, celebramos e relembrando momentos marcantes
              por meio de uma linha do tempo, depoimentos emocionantes e eventos comemorativos.
              Mais do que uma celebração, essa data simboliza o compromisso contínuo com a
              inclusão, o respeito e a construção de uma sociedade mais justa para todos.
            </p>
            <Link href="/pages/30anos">
              <button className={styles.ctaButton}>30 Anos da APAE</button>
            </Link>
          </div>
        </section>

        <section className={styles.partners}>
          <h4>Parceiros</h4>
          <Image
            src="/parceiro-escudo.png"
            alt="Parceiro"
            width={70}
            height={100}
          />
        </section>
      </main>
    </>
  );
}
"use client";

import React, { useEffect, useState } from 'react';
import styles from './LeitorDeTexto.module.css';
import { Play,Pause,CircleStop,Venus,Mars  } from 'lucide-react';


const LeitorDeTexto: React.FC<{ texto: string }> = ({ texto }) => {
    const [vozMasculina, setVozMasculina] = useState(true);
    const [lendo, setLendo] = useState(false);
    const [pausado, setPausado] = useState(false);

    const iniciarLeitura = () => {
        if ('speechSynthesis' in window) {
            window.speechSynthesis.cancel();

            const utterance = new SpeechSynthesisUtterance(texto);
            utterance.lang = 'pt-BR';
            const voices = speechSynthesis.getVoices();
            const voz = voices[vozMasculina ? 0 : 1];
            utterance.voice = voz;

            utterance.onstart = () => {
                setLendo(true);
                setPausado(false);
            };
            utterance.onend = () => {
                setLendo(false);
                setPausado(false);
            };
            utterance.onerror = () => {
                setLendo(false);
                setPausado(false);
            };

            window.speechSynthesis.speak(utterance);
        } else {
            alert("Seu navegador não suporta leitura de texto em voz alta.");
        }
    };

    const pararLeitura = () => {
        window.speechSynthesis.cancel();
        setLendo(false);
        setPausado(false);
    };

    const pausarLeitura = () => {
        if (window.speechSynthesis.speaking && !window.speechSynthesis.paused) {
            window.speechSynthesis.pause();
            setPausado(true);
        }
    };

    const retomarLeitura = () => {
        if (window.speechSynthesis.paused) {
            window.speechSynthesis.resume();
            setPausado(false);
        }
    };

    useEffect(() => {
        return () => {
            window.speechSynthesis.cancel();
        };
    }, []);

    return (
        <div className={styles.container}>
            <div className={styles.buttonGroup}>
                <button
                    onClick={() => setVozMasculina(true)}
                    className={`${styles.button} ${styles.buttonVoice} ${vozMasculina ? styles.buttonVoiceActive : ''}`}
                >
                    <Mars className={styles.icon}/>
                    Voz Masculina
                </button>
                <button
                    onClick={() => setVozMasculina(false)}
                    className={`${styles.button} ${styles.buttonVoice} ${!vozMasculina ? styles.buttonVoiceActive : ''}`}
                    
                >
                    <Venus />
                    Voz Feminina
                </button>
            </div>

            <div className={styles.buttonGroup}>
                {!lendo ? (
                    <button onClick={iniciarLeitura} className={`${styles.button} ${styles.buttonActive}`}>
                        <Play />
                        Iniciar
                    </button>
                ) : (
                    <button onClick={pararLeitura} className={`${styles.button} ${styles.buttonStop}`}>
                        <CircleStop />
                        Parar
                    </button>
                )}

                {lendo && !pausado && (
                    <button onClick={pausarLeitura} className={`${styles.button} ${styles.buttonPause}`}>
                        <Pause />
                        Pausar
                    </button>
                )}

                {pausado && (
                    <button onClick={retomarLeitura} className={`${styles.button} ${styles.buttonResume}`}>
                        <Play />
                        Retomar
                    </button>
                )}
            </div>
        </div>
    );
};

export default LeitorDeTexto;

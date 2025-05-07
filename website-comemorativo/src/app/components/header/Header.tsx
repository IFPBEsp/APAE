'use client';

import { usePathname } from 'next/navigation';
import Image from "next/image";
import Styles from "./page.module.css";
import { AlignJustify } from 'lucide-react';
import { useState } from 'react';

export default function Header() {
    const pathname = usePathname();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    const toggleMenu = () => {
        setIsMenuOpen(!isMenuOpen);
    }

    return (
        <>
            <div className={Styles.header}>
                <Image src="/APAE-logo.png" alt="Logo APAE" width={50} height={50} />
                <div className={Styles.linksTelas}>
                    <span className={pathname == "/" ? Styles.pageLink : Styles.link}>Página Inicial</span>
                    <span className={pathname == "/30anos" ? Styles.pageLink : Styles.link}>30 Anos</span>
                    <span className={pathname == "/contato" ? Styles.pageLink : Styles.link}>Contato</span>
                </div>
                <button className={Styles.hamburguer} onClick={toggleMenu}>
                    <AlignJustify />
                </button>
            </div>
            <div className={Styles.darkScreen}></div>
            <div className={isMenuOpen ? Styles.menuOpen : Styles.menu}>
                <p>Página Inicial</p>
                <p>30 Anos</p>
                <p>Contato</p>
            </div>
        </>
    );
}
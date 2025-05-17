"use client";

import { usePathname } from "next/navigation";
import Image from "next/image";
import Styles from "./Header.module.css";
import { AlignJustify } from "lucide-react";
import { useState } from "react";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";
import Link from "next/link";

export default function Header() {
	const pathname = usePathname();
	const [isMenuOpen, setIsMenuOpen] = useState(false);

	const toggleMenu = () => {
		setIsMenuOpen(!isMenuOpen);
	};

	return (
		<>
			<div className={Styles.header}>
				<Image src="/logo-apae.png" alt="Logo APAE" width={120} height={50} />
				<div className={Styles.linksTelas}>
					<Link href="/pages/TelaInicial" className={pathname == "/pages/TelaInicial" ? Styles.linkAtivo : Styles.link}>
						Página Inicial
					</Link>
					<Link
						href="/pages/30anos"
						className={pathname == "/pages/30anos" ? Styles.linkAtivo : Styles.link}
					>
						30 Anos
					</Link>
					<Link
						href="/pages/contato"
						className={pathname == "/pages/contato" ? Styles.linkAtivo : Styles.link}
					>
						Contato
					</Link>
				</div>
				<button className={Styles.hamburguer} onClick={toggleMenu}>
					<AlignJustify color="#0D4F97" />
				</button>
			</div>
			<SwipeableDrawer
				anchor="right"
				open={isMenuOpen}
				onClose={toggleMenu}
				onOpen={() => 0}
			>
				<ul className={Styles.menu}>
					<li>
						<Link href="/pages/TelaInicial">Página Inicial</Link>
					</li>
					<li>
						<Link href="/pages/30anos">30 Anos</Link>
					</li>
					<li>
						<Link href="/pages/contato">Contato</Link>
					</li>
				</ul>
			</SwipeableDrawer>
		</>
	);
}

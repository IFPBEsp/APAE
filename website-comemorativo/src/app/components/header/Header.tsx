"use client";

import { usePathname } from "next/navigation";
import Image from "next/image";
import Styles from "./page.module.css";
import { AlignJustify } from "lucide-react";
import { useState } from "react";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";

export default function Header() {
	const pathname = usePathname();
	const [isMenuOpen, setIsMenuOpen] = useState(false);

	const toggleMenu = () => {
		setIsMenuOpen(!isMenuOpen);
	};

	return (
		<>
			<div className={Styles.header}>
				<Image src="/logo.png" alt="Logo APAE" width={120} height={50} />
				<div className={Styles.linksTelas}>
					<span className={pathname == "/" ? Styles.linkAtivo : Styles.link}>
						Página Inicial
					</span>
					<span
						className={pathname == "/30anos" ? Styles.pageLink : Styles.link}
					>
						30 Anos
					</span>
					<span
						className={pathname == "/contato" ? Styles.pageLink : Styles.link}
					>
						Contato
					</span>
				</div>
				<button className={Styles.hamburguer} onClick={toggleMenu}>
					<AlignJustify />
				</button>
			</div>
			<SwipeableDrawer
				anchor="right"
				open={isMenuOpen}
				onClose={toggleMenu}
				onOpen={() => 0}
			>
				<ul className={Styles.menu}>
					<li>Página Inicial</li>
					<li>30 Anos</li>
					<li>Contato</li>
				</ul>
			</SwipeableDrawer>
		</>
	);
}

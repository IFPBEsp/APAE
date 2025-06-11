"use client";

import { usePathname } from "next/navigation";
import Image from "next/image";
import styles from "./Header.module.css";
import {
  AlignJustify,
  Accessibility,
  Contrast,
  EyeOff,
  Text,
} from "lucide-react";
import { useState } from "react";
import SwipeableDrawer from "@mui/material/SwipeableDrawer";
import Link from "next/link";
import Popover from "@mui/material/Popover";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import Switch from "@mui/material/Switch";

export default function Header() {
  const pathname = usePathname();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
  const handleLinkClick = () => setIsMenuOpen(false);
  const handleAccessibilityClick = (event: React.MouseEvent<HTMLElement>) =>
    setAnchorEl(event.currentTarget);
  const handleClose = () => setAnchorEl(null);

  const open = Boolean(anchorEl);
  const id = open ? "accessibility-popover" : undefined;

  return (
    <>
      <div className={styles.header}>
        <Image src="/logo-apae.png" alt="Logo APAE" width={120} height={50} />

        <div className={styles.linksTelas}>
          <Link
            href="/pages/TelaInicial"
            className={
              pathname === "/pages/TelaInicial"
                ? styles.linkAtivo
                : styles.link
            }
          >
            Página Inicial
          </Link>
          <Link
            href="/pages/30anos"
            className={
              pathname === "/pages/30anos" ? styles.linkAtivo : styles.link
            }
          >
            30 Anos
          </Link>
          <Link
            href="/pages/contato"
            className={
              pathname === "/pages/contato" ? styles.linkAtivo : styles.link
            }
          >
            Contato
          </Link>
        </div>

        {/* Botão Acessibilidade */}
        <button
          className={styles.accessibilityButton}
          onClick={handleAccessibilityClick}
          aria-describedby={id}
          aria-label="Acessibilidade"
          type="button"
        >
          <Accessibility size={20} />
        </button>

        {/* Botão Hamburguer */}
        <button className={styles.hamburguer} onClick={toggleMenu} type="button">
          <AlignJustify color="#0D4F97" />
        </button>
      </div>

      {/* Popover de Acessibilidade */}
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        container={typeof window !== "undefined" ? document.body : undefined}
        anchorOrigin={{
          vertical: "bottom",
          horizontal: "right",
        }}
        transformOrigin={{
          vertical: "top",
          horizontal: "right",
        }}
        PaperProps={{
          sx: {
            position: "fixed",
            zIndex: 1400,
            mt: 1,
          },
        }}
        disableScrollLock
        disableRestoreFocus
      >
        <div className={styles.accessibilityBox}>
          <Typography variant="subtitle1" fontWeight={600} mb={1}>
            Acessibilidade
          </Typography>

          <div className={styles.accessibilityOption}>
            <div className={styles.accessibilityIconText}>
              <Contrast size={18} />
              <span>Modo Alto Contraste</span>
            </div>
            <Switch disabled />
          </div>

          <div className={styles.accessibilityOption}>
            <div className={styles.accessibilityIconText}>
              <EyeOff size={18} />
              <span>Escala de Cinza</span>
            </div>
            <Switch disabled />
          </div>

          <div className={styles.accessibilityOption}>
            <div className={styles.accessibilityIconText}>
              <Text size={18} />
              <span>Tamanho da Fonte</span>
            </div>
            <div>
              <Button size="small" disabled>
                A-
              </Button>
              <Button size="small" disabled>
                A+
              </Button>
            </div>
          </div>
        </div>
      </Popover>

      {/* Menu lateral para mobile */}
      <SwipeableDrawer
        anchor="right"
        open={isMenuOpen}
        onClose={toggleMenu}
        onOpen={() => {}}
        disableSwipeToOpen
      >
        <ul className={styles.menu}>
          <li>
            <Link href="/pages/TelaInicial" onClick={handleLinkClick}>
              Página Inicial
            </Link>
          </li>
          <li>
            <Link href="/pages/30anos" onClick={handleLinkClick}>
              30 Anos
            </Link>
          </li>
          <li>
            <Link href="/pages/contato" onClick={handleLinkClick}>
              Contato
            </Link>
          </li>
        </ul>
      </SwipeableDrawer>
    </>
  );
}

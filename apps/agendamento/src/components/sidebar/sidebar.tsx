import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarHeader,
} from "@/components/ui/sidebar";
import { HomeIcon, TasklistIcon, CalendarIcon } from '@primer/octicons-react';
import styles from "./sidebar.module.css";
import Image from "next/image";

export function AppSidebar() {
    return (
        <Sidebar className={styles.sidebar}>
            <SidebarHeader className={styles.header}>
                <Image className={styles.logo} src={require("../../assets/APAE-logo.svg")} width={35} height={35} alt="Logo da APAE" />
                <div>
                    <div className={styles.title}>APAE</div>
                    <div className={styles.subtitle}>Agendamento</div>
                </div>
            </SidebarHeader>
            <SidebarContent>
                <button className={styles.listItem}>
                    <HomeIcon size={20} />
                    <span>Tela inicial</span>
                </button>
                <button className={styles.listItem}>
                    <TasklistIcon size={20} />
                    <span>Todos os agendamentos</span>
                </button>
                <button className={styles.listItem}>
                    <CalendarIcon size={20} />
                    <span>Calendário</span>
                </button>
            </SidebarContent>
            <SidebarFooter />
        </Sidebar>
    )
}
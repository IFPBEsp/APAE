"use client";

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
            <div>
                <button className={styles.listItem}>
                <HomeIcon size={20} />
                <span>Agendamentos</span>
                </button>
                <div className="ml-8 mt-1 flex flex-col gap-1 text-sm text-gray-700">
                <button className={`${styles.listItem} pl-6`}>
                    <TasklistIcon size={16} />
                    <span>Todos os agendamentos</span>
                </button>
                <button className={`${styles.listItem} pl-6`}>
                    <CalendarIcon size={16} />
                    <span>Calendário</span>
                </button>
                </div>
            </div>
            </SidebarContent>
            <SidebarFooter />
        </Sidebar>
    )
}
import {
    Sidebar,
    SidebarContent,
    SidebarFooter,
    SidebarGroup,
    SidebarHeader,
} from "@/components/ui/sidebar";
import { HomeIcon } from '@primer/octicons-react';
import styles from "./sidebar.module.css";

export function AppSidebar() {
    return (
        <Sidebar>
            <SidebarHeader>
                <div>Título</div>
            </SidebarHeader>
            <SidebarContent>
                <SidebarGroup className={styles.listItem}>
                    <HomeIcon size={20}/>
                    <span>Dashboard</span>
                </SidebarGroup>
            </SidebarContent>
            <SidebarFooter />
        </Sidebar>
    )
}
import { Outlet } from "react-router-dom"
import { CalendarDays, ClipboardList, LayoutDashboard, Settings, Users } from "lucide-react"
import { NavLink } from "react-router-dom"

import { DashboardHeader } from "@/components/dashboard/dashboard-header"
import { DashboardSidebar } from "@/components/dashboard/dashboard-sidebar"

export function AppLayout() {
  return (
    <div className="flex min-h-svh bg-background">
      <DashboardSidebar />
      <div className="flex min-w-0 flex-1 flex-col">
        <DashboardHeader />
        <Outlet />
        <nav className="sticky bottom-0 z-20 grid grid-cols-5 border-t border-border bg-card/95 px-1 py-2 backdrop-blur lg:hidden" aria-label="Navegação móvel">
          {[
            ["/dashboard", "Início", LayoutDashboard],
            ["/pacientes", "Pacientes", Users],
            ["/consultas", "Consultas", CalendarDays],
            ["/avaliacoes", "Avaliações", ClipboardList],
            ["/configuracoes", "Ajustes", Settings],
          ].map(([to, label, Icon]) => {
            const IconComponent = Icon as typeof LayoutDashboard
            return <NavLink key={to as string} to={to as string} className={({isActive})=>`flex flex-col items-center gap-1 rounded-lg py-1.5 text-[10px] ${isActive?"text-primary":"text-muted-foreground"}`}><IconComponent className="size-4"/>{label as string}</NavLink>
          })}
        </nav>
      </div>
    </div>
  )
}

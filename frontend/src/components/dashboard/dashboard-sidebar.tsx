import {
  CalendarDays,
  ClipboardList,
  LayoutDashboard,
  LogOut,
  Settings,
  Users,
} from "lucide-react"

import { NutriLogo } from "@/components/common/nutri-logo"
import { useAuth } from "@/contexts/auth-context"

const navItems = [
  { label: "Dashboard", icon: LayoutDashboard, ativo: true },
  { label: "Pacientes", icon: Users, ativo: false },
  { label: "Consultas", icon: CalendarDays, ativo: false },
  { label: "Avaliações", icon: ClipboardList, ativo: false },
  { label: "Configurações", icon: Settings, ativo: false },
]

export function DashboardSidebar() {
  const { logout } = useAuth()

  return (
    <aside className="hidden w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar lg:flex">
      <div className="flex h-16 items-center border-b border-sidebar-border px-6">
        <NutriLogo />
      </div>

      <nav className="flex flex-1 flex-col gap-1 p-4" aria-label="Navegação principal">
        {navItems.map(({ label, icon: Icon, ativo }) => (
          <button
            key={label}
            type="button"
            disabled={!ativo}
            title={!ativo ? "Tela ainda não disponível" : undefined}
            className={
              ativo
                ? "flex items-center gap-3 rounded-xl bg-sidebar-primary px-3 py-2.5 text-sm font-medium text-sidebar-primary-foreground shadow-sm"
                : "flex cursor-not-allowed items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-sidebar-foreground opacity-55"
            }
            aria-current={ativo ? "page" : undefined}
          >
            <Icon className="size-[18px]" aria-hidden="true" />
            {label}
          </button>
        ))}
      </nav>

      <div className="border-t border-sidebar-border p-4">
        <button
          type="button"
          onClick={() => void logout()}
          className="flex w-full items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-sidebar-foreground transition-colors hover:bg-destructive/10 hover:text-destructive"
        >
          <LogOut className="size-[18px]" aria-hidden="true" />
          Sair
        </button>
      </div>
    </aside>
  )
}

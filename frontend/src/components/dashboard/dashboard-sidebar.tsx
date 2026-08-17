import {
  CalendarDays,
  ClipboardList,
  LayoutDashboard,
  LogOut,
  Settings,
  Users,
} from "lucide-react";
import { NavLink } from "react-router-dom";

import { NutriLogo } from "@/components/common/nutri-logo";
import { useAuth } from "@/contexts/auth-context";

const navItems = [
  { label: "Dashboard", icon: LayoutDashboard, to: "/dashboard" },
  { label: "Pacientes", icon: Users, to: "/pacientes" },
  { label: "Consultas", icon: CalendarDays, to: "/consultas" },
  { label: "Avaliações", icon: ClipboardList, to: "/avaliacoes" },
  { label: "Configurações", icon: Settings, to: "/configuracoes" },
];

export function DashboardSidebar() {
  const { logout } = useAuth();

  return (
    <aside className="hidden w-64 shrink-0 flex-col border-r border-sidebar-border bg-sidebar lg:flex">
      <div className="flex h-16 items-center border-b border-sidebar-border px-6">
        <NutriLogo />
      </div>

      <nav
        className="flex flex-1 flex-col gap-1 p-4"
        aria-label="Navegação principal"
      >
        {navItems.map(({ label, icon: Icon, to }) => (
          <NavLink
            key={label}
            to={to}
            className={({ isActive }) =>
              isActive
                ? "flex items-center gap-3 rounded-xl bg-sidebar-primary px-3 py-2.5 text-sm font-medium text-sidebar-primary-foreground shadow-sm"
                : "flex items-center gap-3 rounded-xl px-3 py-2.5 text-sm font-medium text-sidebar-foreground transition-colors hover:bg-sidebar-accent hover:text-sidebar-accent-foreground"
            }
          >
            <Icon className="size-[18px]" aria-hidden="true" />
            {label}
          </NavLink>
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
  );
}

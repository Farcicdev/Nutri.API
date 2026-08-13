import { LogOut } from "lucide-react"

import { NutriLogo } from "@/components/common/nutri-logo"
import { ThemeToggle } from "@/components/common/theme-toggle"
import { useAuth } from "@/contexts/auth-context"

function saudacaoAtual() {
  const hora = new Date().getHours()
  if (hora < 12) return "Bom dia"
  if (hora < 18) return "Boa tarde"
  return "Boa noite"
}

function iniciais(nome: string) {
  return nome
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((parte) => parte[0])
    .join("")
    .toUpperCase()
}

export function DashboardHeader() {
  const { usuario, logout } = useAuth()
  const nome = usuario?.nome ?? "Nutricionista"
  const primeiroNome = nome.trim().split(/\s+/)[0]

  return (
    <header className="border-b border-border bg-card/60 px-5 py-4 backdrop-blur sm:px-8">
      <div className="mb-4 flex items-center justify-between lg:hidden">
        <NutriLogo />
        <div className="flex items-center gap-2">
          <ThemeToggle />
          <button
            type="button"
            onClick={() => void logout()}
            className="flex size-10 items-center justify-center rounded-xl border border-border text-muted-foreground transition-colors hover:text-destructive"
            aria-label="Sair"
          >
            <LogOut className="size-5" />
          </button>
        </div>
      </div>

      <div className="flex items-center justify-between gap-4">
        <div className="min-w-0">
          <h1 className="font-serif text-2xl font-bold tracking-tight text-foreground">
            {saudacaoAtual()}, {primeiroNome}
          </h1>
          <p className="mt-0.5 text-sm text-muted-foreground">
            Aqui está o resumo da sua clínica hoje.
          </p>
        </div>

        <div className="hidden items-center gap-3 sm:flex">
          <ThemeToggle />
          <div className="flex size-10 items-center justify-center rounded-full bg-primary/15 text-sm font-semibold text-primary">
            {iniciais(nome)}
          </div>
          <div className="hidden flex-col leading-tight md:flex">
            <span className="max-w-52 truncate text-sm font-semibold">{nome}</span>
            <span className="text-xs text-muted-foreground">Nutricionista</span>
          </div>
        </div>
      </div>
    </header>
  )
}

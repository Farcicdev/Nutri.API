import { Moon, Sun } from "lucide-react"

import { useTheme } from "@/contexts/theme-context"
import { cn } from "@/lib/utils"

export function ThemeToggle({ className }: { className?: string }) {
  const { theme, alternarTema } = useTheme()
  const modoEscuro = theme === "dark"

  return (
    <button
      type="button"
      onClick={alternarTema}
      className={cn(
        "flex size-10 items-center justify-center rounded-xl border border-border bg-card text-muted-foreground shadow-sm transition-colors hover:bg-accent hover:text-foreground",
        className,
      )}
      aria-label={modoEscuro ? "Ativar tema claro" : "Ativar tema escuro"}
      title={modoEscuro ? "Tema claro" : "Tema escuro"}
    >
      {modoEscuro ? <Sun className="size-5" aria-hidden="true" /> : <Moon className="size-5" aria-hidden="true" />}
    </button>
  )
}

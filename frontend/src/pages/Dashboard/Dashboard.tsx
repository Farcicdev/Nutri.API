import { useAuth } from "@/contexts/auth-context"

export default function Dashboard() {
  const { usuario, logout } = useAuth()

  return (
    <main className="min-h-svh bg-background p-8 text-foreground">
      <div className="mx-auto flex max-w-5xl items-center justify-between">
        <div>
          <h1 className="font-serif text-3xl font-bold">Dashboard</h1>
          <p className="mt-2 text-muted-foreground">
            Bem-vindo, {usuario?.nome ?? "usuário"}.
          </p>
        </div>

        <button
          type="button"
          onClick={() => void logout()}
          className="rounded-md border border-border px-4 py-2 text-sm font-medium transition-colors hover:bg-accent"
        >
          Sair
        </button>
      </div>
    </main>
  )
}

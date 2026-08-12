import { Navigate, Outlet } from "react-router-dom"

import { useAuth } from "@/contexts/auth-context"

export function PublicOnlyRoute() {
    const { autenticado, carregando } = useAuth()

    if (carregando) {
        return (
            <main className="flex min-h-svh items-center justify-center">
                <p className="text-muted-foreground">Carregando sua sessão...</p>
            </main>
        )
    }

    if (autenticado) {
        return <Navigate to="/dashboard" replace />
    }

    return <Outlet />
}
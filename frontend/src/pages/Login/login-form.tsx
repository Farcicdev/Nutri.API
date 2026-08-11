"use client"

import type React from "react"
import { useState } from "react"
import { Eye, EyeOff, Mail, Lock } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Checkbox } from "@/components/ui/checkbox"
import { entrar, mensagemDaApi } from "@/lib/api"

interface LoginCredentials {
    email: string
    password: string
    rememberMe: boolean
}

export function LoginForm() {
    const [credentials, setCredentials] = useState<LoginCredentials>({
        email: "",
        password: "",
        rememberMe: false,
    })
    const [showPassword, setShowPassword] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [errorMessage, setErrorMessage] = useState<string | null>(null)
    const [successMessage, setSuccessMessage] = useState<string | null>(null)

    async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault()
        setIsSubmitting(true)
        setErrorMessage(null)
        setSuccessMessage(null)

        try {
            await entrar({
                email: credentials.email,
                senha: credentials.password,
                lembrarDeMim: credentials.rememberMe,
            })
            setSuccessMessage("Login realizado com sucesso.")
        } catch (error) {
            setErrorMessage(mensagemDaApi(error))
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <form onSubmit={handleSubmit} className="flex flex-col gap-5">
            <div className="flex flex-col gap-2">
                <Label htmlFor="email">E-mail</Label>
                <div className="relative">
                    <Mail
                        className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground"
                        aria-hidden="true"
                    />
                    <Input
                        id="email"
                        name="email"
                        type="email"
                        autoComplete="email"
                        placeholder="voce@nutriapp.com"
                        className="pl-10"
                        value={credentials.email}
                        onChange={(e) => setCredentials((prev) => ({ ...prev, email: e.target.value }))}
                        required
                    />
                </div>
            </div>

            <div className="flex flex-col gap-2">
                <Label htmlFor="password">Senha</Label>
                <div className="relative">
                    <Lock
                        className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground"
                        aria-hidden="true"
                    />
                    <Input
                        id="password"
                        name="password"
                        type={showPassword ? "text" : "password"}
                        autoComplete="current-password"
                        placeholder="Digite sua senha"
                        className="pl-10 pr-10"
                        value={credentials.password}
                        onChange={(e) => setCredentials((prev) => ({ ...prev, password: e.target.value }))}
                        required
                    />
                    <button
                        type="button"
                        onClick={() => setShowPassword((v) => !v)}
                        className="absolute right-3 top-1/2 -translate-y-1/2 rounded-sm text-muted-foreground transition-colors hover:text-foreground focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                        aria-label={showPassword ? "Ocultar senha" : "Mostrar senha"}
                    >
                        {showPassword ? <EyeOff className="size-4" /> : <Eye className="size-4" />}
                    </button>
                </div>
            </div>

            <div className="flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <Checkbox
                        id="remember"
                        checked={credentials.rememberMe}
                        onCheckedChange={(checked) =>
                            setCredentials((prev) => ({ ...prev, rememberMe: checked === true }))
                        }
                    />
                    <Label htmlFor="remember" className="cursor-pointer text-sm font-normal text-muted-foreground">
                        Lembrar de mim
                    </Label>
                </div>
                <a
                    href="#"
                    className="text-sm font-medium text-primary transition-colors hover:text-primary/80"
                >
                    Esqueci minha senha
                </a>
            </div>

            {errorMessage && (
                <p role="alert" className="text-sm text-destructive">
                    {errorMessage}
                </p>
            )}
            {successMessage && (
                <p role="status" className="text-sm text-primary">
                    {successMessage}
                </p>
            )}

            <Button type="submit" size="lg" className="mt-1 w-full font-medium" disabled={isSubmitting}>
                {isSubmitting ? "Entrando..." : "Entrar"}
            </Button>
        </form>
    )
}

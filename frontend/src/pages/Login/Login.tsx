import { LoginForm } from "./login-form"
import { NutriLogo } from "@/components/common/nutri-logo"

export default function LoginPage() {
    return (
        <main className="relative flex min-h-svh items-center justify-center overflow-hidden bg-background px-4 py-10">
            {/* Fundo sutil */}
            <div
                aria-hidden="true"
                className="pointer-events-none absolute inset-0 -z-10"
            >
                <div className="absolute -top-32 -right-24 size-96 rounded-full bg-primary/10 blur-3xl" />
                <div className="absolute -bottom-40 -left-24 size-96 rounded-full bg-accent/40 blur-3xl" />
                <div className="absolute inset-0 bg-[radial-gradient(oklch(0.58_0.13_152_/_0.05)_1px,transparent_1px)] [background-size:22px_22px]" />
            </div>

            <div className="w-full max-w-md">
                <div className="mb-8 flex justify-center">
                    <NutriLogo />
                </div>

                <section className="rounded-3xl border border-border/70 bg-card p-8 shadow-xl shadow-primary/5 sm:p-10">
                    <header className="mb-8 flex flex-col gap-1.5 text-center">
                        <h1 className="font-serif text-3xl font-bold tracking-tight text-card-foreground text-balance">
                            Bem-vindo
                        </h1>
                        <p className="text-sm leading-relaxed text-muted-foreground text-pretty">
                            Entre com sua conta para acessar o painel de gestão nutricional.
                        </p>
                    </header>

                    <LoginForm />
                </section>

                <p className="mt-6 text-center text-sm text-muted-foreground">
                    Ainda não tem uma conta?{" "}
                    <a href="#" className="font-medium text-primary transition-colors hover:text-primary/80">
                        Fale com o suporte
                    </a>
                </p>
            </div>
        </main>
    )
}
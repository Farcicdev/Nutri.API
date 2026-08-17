import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ThemeToggle } from "@/components/common/theme-toggle";
import { useAuth } from "@/contexts/auth-context";

export default function Configuracoes() {
  const { usuario } = useAuth();
  return (
    <main className="flex-1 space-y-6 p-5 sm:p-8">
      <div>
        <h1 className="font-serif text-2xl font-bold">Configurações</h1>
        <p className="text-sm text-muted-foreground">
          Preferências da sua conta e da interface.
        </p>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Minha conta</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div>
              <p className="text-xs text-muted-foreground">Nome</p>
              <p className="font-medium">{usuario?.nome}</p>
            </div>
            <div>
              <p className="text-xs text-muted-foreground">E-mail</p>
              <p className="font-medium">{usuario?.email}</p>
            </div>
            <p className="rounded-lg bg-muted p-3 text-xs text-muted-foreground">
              A edição do perfil e da senha ainda não está disponível para
              nutricionistas no backend.
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Aparência</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex items-center justify-between">
              <div>
                <p className="font-medium">Tema da interface</p>
                <p className="text-sm text-muted-foreground">
                  Alterne entre os modos claro e escuro.
                </p>
              </div>
              <ThemeToggle />
            </div>
          </CardContent>
        </Card>
      </div>
    </main>
  );
}

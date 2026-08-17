import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import type { PacienteRecente } from "@/lib/api";

function iniciais(nome: string) {
  return nome
    .split(/\s+/)
    .slice(0, 2)
    .map((parte) => parte[0])
    .join("")
    .toUpperCase();
}

function formatarData(data: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    day: "2-digit",
    month: "2-digit",
  }).format(new Date(data));
}

export function RecentPatients({
  pacientes,
}: {
  pacientes: PacienteRecente[];
}) {
  return (
    <Card className="h-full">
      <CardHeader>
        <CardTitle className="font-serif text-lg">Pacientes recentes</CardTitle>
      </CardHeader>
      <CardContent className="flex flex-col gap-3">
        {pacientes.length === 0 && (
          <p className="py-10 text-center text-sm text-muted-foreground">
            Nenhum atendimento recente.
          </p>
        )}

        {pacientes.map((paciente) => (
          <div
            key={paciente.id}
            className="flex items-center gap-3 rounded-xl border border-transparent p-1.5 hover:border-border"
          >
            <div className="flex size-10 shrink-0 items-center justify-center rounded-full bg-secondary text-xs font-semibold text-secondary-foreground">
              {iniciais(paciente.nome)}
            </div>
            <div className="min-w-0 flex-1">
              <div className="flex items-center justify-between gap-2">
                <p className="truncate text-sm font-semibold">
                  {paciente.nome}
                </p>
                <span className="shrink-0 text-xs text-muted-foreground">
                  {formatarData(paciente.ultimaConsulta)}
                </span>
              </div>
              <p className="mt-1 text-xs text-muted-foreground">
                IMC: {paciente.imcAtual?.toFixed(1) ?? "—"}
                <span className="mx-1.5">•</span>
                Gordura:{" "}
                {paciente.percentualGorduraAtual != null
                  ? `${paciente.percentualGorduraAtual.toFixed(1)}%`
                  : "—"}
              </p>
            </div>
          </div>
        ))}
      </CardContent>
    </Card>
  );
}

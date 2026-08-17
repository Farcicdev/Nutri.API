import { Clock } from "lucide-react";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { cn } from "@/lib/utils";
import type { ProximaConsulta, StatusConsulta, TipoConsulta } from "@/lib/api";

const statusLabels: Record<StatusConsulta, string> = {
  AGENDADA: "Agendada",
  REALIZADA: "Realizada",
  CANCELADA: "Cancelada",
};

const statusStyles: Record<StatusConsulta, string> = {
  AGENDADA: "border-primary/20 bg-primary/10 text-primary",
  REALIZADA: "border-border bg-accent text-accent-foreground",
  CANCELADA: "border-destructive/20 bg-destructive/10 text-destructive",
};

const tipoLabels: Record<TipoConsulta, string> = {
  PRIMEIRA_CONSULTA: "Primeira consulta",
  RETORNO: "Retorno",
  ACOMPANHAMENTO: "Acompanhamento",
};

function dataHora(data: string) {
  const valor = new Date(data);
  return {
    data: new Intl.DateTimeFormat("pt-BR", {
      day: "2-digit",
      month: "short",
    }).format(valor),
    hora: new Intl.DateTimeFormat("pt-BR", {
      hour: "2-digit",
      minute: "2-digit",
    }).format(valor),
  };
}

export function UpcomingAppointments({
  consultas,
}: {
  consultas: ProximaConsulta[];
}) {
  return (
    <Card>
      <CardHeader>
        <CardTitle className="font-serif text-lg">Próximas consultas</CardTitle>
      </CardHeader>
      <CardContent className="flex flex-col gap-2">
        {consultas.length === 0 && (
          <p className="py-10 text-center text-sm text-muted-foreground">
            Nenhuma consulta agendada.
          </p>
        )}

        {consultas.map((consulta) => {
          const horario = dataHora(consulta.dataConsulta);
          return (
            <div
              key={consulta.id}
              className="flex items-center gap-3 rounded-xl border border-border bg-background/50 p-3 transition-colors hover:bg-accent/40 sm:gap-4"
            >
              <div className="flex size-14 shrink-0 flex-col items-center justify-center rounded-lg bg-primary/10 text-primary">
                <span className="text-[10px] font-medium uppercase">
                  {horario.data}
                </span>
                <span className="mt-0.5 flex items-center gap-1 text-xs font-semibold">
                  <Clock className="size-3" aria-hidden="true" />
                  {horario.hora}
                </span>
              </div>
              <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-semibold">
                  {consulta.pacienteNome}
                </p>
                <p className="truncate text-xs text-muted-foreground">
                  {tipoLabels[consulta.tipo]}
                </p>
              </div>
              <span
                className={cn(
                  "shrink-0 rounded-full border px-2.5 py-1 text-xs font-medium",
                  statusStyles[consulta.status],
                )}
              >
                {statusLabels[consulta.status]}
              </span>
            </div>
          );
        })}
      </CardContent>
    </Card>
  );
}

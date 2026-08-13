import { CalendarCheck, CalendarClock, ClipboardList, TrendingUp, Users } from "lucide-react"

import { Card } from "@/components/ui/card"
import type { DashboardResumo } from "@/lib/api"

export function StatCards({ resumo }: { resumo: DashboardResumo }) {
  const cards = [
    {
      label: "Total de pacientes",
      valor: resumo.totalPacientes,
      detalhe: "Pacientes ativos",
      icon: Users,
    },
    {
      label: "Consultas de hoje",
      valor: resumo.consultasHoje,
      detalhe: `${resumo.consultasConfirmadasHoje} agendadas`,
      icon: CalendarCheck,
    },
    {
      label: "Próximas consultas",
      valor: resumo.consultasProximosSeteDias,
      detalhe: "Próximos 7 dias",
      icon: CalendarClock,
    },
    {
      label: "Avaliações realizadas",
      valor: resumo.avaliacoesRealizadas,
      detalhe: `${resumo.avaliacoesRealizadasNoMes} neste mês`,
      icon: ClipboardList,
    },
  ]

  return (
    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
      {cards.map(({ label, valor, detalhe, icon: Icon }) => (
        <Card key={label} className="gap-0 p-5">
          <div className="flex items-start justify-between">
            <span className="text-sm font-medium text-muted-foreground">{label}</span>
            <div className="flex size-10 items-center justify-center rounded-xl bg-primary/10 text-primary">
              <Icon className="size-5" aria-hidden="true" />
            </div>
          </div>
          <p className="mt-3 font-serif text-3xl font-bold tracking-tight">{valor}</p>
          <div className="mt-2 flex items-center gap-1.5 text-xs font-medium text-primary">
            <TrendingUp className="size-3.5" aria-hidden="true" />
            {detalhe}
          </div>
        </Card>
      ))}
    </div>
  )
}

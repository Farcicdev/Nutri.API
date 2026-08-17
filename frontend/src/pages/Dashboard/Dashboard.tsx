import { useCallback, useEffect, useState } from "react";

import { AttendanceChart } from "@/components/dashboard/attendance-chart";
import { RecentPatients } from "@/components/dashboard/recent-patients";
import { StatCards } from "@/components/dashboard/stat-cards";
import { UpcomingAppointments } from "@/components/dashboard/upcoming-appointments";
import { Button } from "@/components/ui/button";
import {
  buscarResumoDashboard,
  mensagemDaApi,
  type DashboardResumo,
} from "@/lib/api";

export default function Dashboard() {
  const [resumo, setResumo] = useState<DashboardResumo | null>(null);
  const [carregando, setCarregando] = useState(true);
  const [erro, setErro] = useState<string | null>(null);

  const carregarResumo = useCallback(async () => {
    setCarregando(true);
    setErro(null);

    try {
      setResumo(await buscarResumoDashboard());
    } catch (error) {
      setErro(mensagemDaApi(error));
    } finally {
      setCarregando(false);
    }
  }, []);

  useEffect(() => {
    void carregarResumo();
  }, [carregarResumo]);

  return (
    <main className="flex-1 space-y-6 p-5 sm:p-8">
      {carregando && <DashboardSkeleton />}

      {!carregando && erro && (
        <section className="flex min-h-72 flex-col items-center justify-center gap-4 rounded-2xl border border-border bg-card p-8 text-center">
          <div>
            <h2 className="font-serif text-xl font-semibold">
              Não foi possível carregar o dashboard
            </h2>
            <p className="mt-1 text-sm text-muted-foreground">{erro}</p>
          </div>
          <Button type="button" onClick={() => void carregarResumo()}>
            Tentar novamente
          </Button>
        </section>
      )}

      {!carregando && resumo && (
        <>
          <StatCards resumo={resumo} />

          <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
            <div className="lg:col-span-2">
              <AttendanceChart atendimentos={resumo.atendimentosPorMes} />
            </div>
            <RecentPatients pacientes={resumo.pacientesRecentes} />
          </div>

          <UpcomingAppointments consultas={resumo.proximasConsultas} />
        </>
      )}
    </main>
  );
}

function DashboardSkeleton() {
  return (
    <div className="animate-pulse space-y-6" aria-label="Carregando dashboard">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {Array.from({ length: 4 }).map((_, index) => (
          <div key={index} className="h-36 rounded-xl bg-muted" />
        ))}
      </div>
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-3">
        <div className="h-96 rounded-xl bg-muted lg:col-span-2" />
        <div className="h-96 rounded-xl bg-muted" />
      </div>
      <div className="h-72 rounded-xl bg-muted" />
    </div>
  );
}

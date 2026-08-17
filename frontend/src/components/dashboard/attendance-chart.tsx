import {
  Area,
  AreaChart,
  CartesianGrid,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";

import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import type { AtendimentoPorMes } from "@/lib/api";

const nomesMeses = [
  "Jan",
  "Fev",
  "Mar",
  "Abr",
  "Mai",
  "Jun",
  "Jul",
  "Ago",
  "Set",
  "Out",
  "Nov",
  "Dez",
];

export function AttendanceChart({
  atendimentos,
}: {
  atendimentos: AtendimentoPorMes[];
}) {
  const dados = atendimentos.map((item) => ({
    mes: nomesMeses[item.mes - 1] ?? String(item.mes),
    atendimentos: item.quantidade,
  }));
  const ano = atendimentos[0]?.ano ?? new Date().getFullYear();

  return (
    <Card className="h-full">
      <CardHeader>
        <CardTitle className="font-serif text-lg">
          Evolução de atendimentos
        </CardTitle>
        <CardDescription>Consultas realizadas por mês em {ano}</CardDescription>
      </CardHeader>
      <CardContent className="h-[300px]">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={dados} margin={{ left: -16, right: 8, top: 8 }}>
            <defs>
              <linearGradient id="fillAtendimentos" x1="0" y1="0" x2="0" y2="1">
                <stop
                  offset="5%"
                  stopColor="var(--chart-1)"
                  stopOpacity={0.35}
                />
                <stop
                  offset="95%"
                  stopColor="var(--chart-1)"
                  stopOpacity={0.02}
                />
              </linearGradient>
            </defs>
            <CartesianGrid
              vertical={false}
              stroke="var(--border)"
              strokeDasharray="3 3"
            />
            <XAxis
              dataKey="mes"
              tickLine={false}
              axisLine={false}
              tickMargin={10}
              fontSize={12}
            />
            <YAxis
              allowDecimals={false}
              tickLine={false}
              axisLine={false}
              width={38}
              fontSize={12}
            />
            <Tooltip
              cursor={{ stroke: "var(--border)" }}
              contentStyle={{
                borderColor: "var(--border)",
                borderRadius: "0.75rem",
                background: "var(--card)",
                color: "var(--card-foreground)",
              }}
            />
            <Area
              dataKey="atendimentos"
              name="Atendimentos"
              type="natural"
              fill="url(#fillAtendimentos)"
              stroke="var(--chart-1)"
              strokeWidth={2.5}
              dot={false}
            />
          </AreaChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
}

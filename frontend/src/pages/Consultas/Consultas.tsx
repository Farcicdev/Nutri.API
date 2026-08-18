import { useEffect, useMemo, useState, type FormEvent } from "react";
import { CalendarPlus, Pencil, Search, Trash2 } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  atualizarConsulta,
  atualizarStatusConsulta,
  criarConsulta,
  listarConsultas,
  listarPacientes,
  mensagemDaApi,
  removerConsulta,
  type Consulta,
  type ConsultaPayload,
  type Paciente,
  type StatusConsulta,
  type TipoConsulta,
} from "@/lib/api";

const campo =
  "min-h-9 w-full rounded-lg border border-input bg-transparent px-3 text-sm outline-none focus:border-ring focus:ring-3 focus:ring-ring/30";
const tipos: Record<TipoConsulta, string> = {
  PRIMEIRA_CONSULTA: "Primeira consulta",
  RETORNO: "Retorno",
  ACOMPANHAMENTO: "Acompanhamento",
};
const status: Record<StatusConsulta, string> = {
  AGENDADA: "Agendada",
  REALIZADA: "Realizada",
  CANCELADA: "Cancelada",
};
function dataApi(valor: string) {
  const [data, hora] = valor.split("T");
  const [a, m, d] = data.split("-");
  return `${d}/${m}/${a} ${hora}`;
}
function dataInput(valor: string) {
  return valor.length >= 16 ? valor.slice(0, 16) : valor;
}
function formatar(valor: string) {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(new Date(valor));
}

export default function Consultas() {
  const [consultas, setConsultas] = useState<Consulta[]>([]),
    [pacientes, setPacientes] = useState<Paciente[]>([]);
  const [busca, setBusca] = useState(""),
    [erro, setErro] = useState<string | null>(null),
    [aberto, setAberto] = useState(false),
    [editando, setEditando] = useState<number | null>(null);
  const [form, setForm] = useState<ConsultaPayload>({
    dataConsulta: "",
    tipo: "PRIMEIRA_CONSULTA",
    observacoes: "",
    pacienteId: 0,
  });
  async function carregar() {
    try {
      const [c, p] = await Promise.all([listarConsultas(), listarPacientes()]);
      setConsultas(c);
      setPacientes(p.filter((x) => x.ativo));
      setErro(null);
    } catch (e) {
      setErro(mensagemDaApi(e));
    }
  }
  useEffect(() => {
    void carregar();
  }, []);
  const filtradas = useMemo(
    () =>
      consultas.filter((c) =>
        c.pacienteNome.toLowerCase().includes(busca.toLowerCase()),
      ),
    [consultas, busca],
  );
  function nova() {
    setEditando(null);
    setForm({
      dataConsulta: "",
      tipo: "PRIMEIRA_CONSULTA",
      observacoes: "",
      pacienteId: pacientes[0]?.id ?? 0,
    });
    setAberto(true);
  }
  function editar(c: Consulta) {
    setEditando(c.id);
    setForm({
      dataConsulta: dataInput(c.dataConsulta),
      tipo: c.tipo,
      observacoes: c.observacoes ?? "",
      pacienteId: c.pacienteId,
    });
    setAberto(true);
  }
  async function salvar(e: FormEvent) {
    e.preventDefault();
    try {
      const payload = { ...form, dataConsulta: dataApi(form.dataConsulta) };
      if (editando) await atualizarConsulta(editando, payload);
      else await criarConsulta(payload);
      setAberto(false);
      await carregar();
    } catch (err) {
      setErro(mensagemDaApi(err));
    }
  }
  async function mudarStatus(id: number, novo: StatusConsulta) {
    try {
      await atualizarStatusConsulta(id, novo);
      await carregar();
    } catch (e) {
      setErro(mensagemDaApi(e));
    }
  }
  async function excluir(c: Consulta) {
    if (!confirm(`Excluir a consulta de ${c.pacienteNome}?`)) return;
    try {
      await removerConsulta(c.id);
      await carregar();
    } catch (e) {
      setErro(mensagemDaApi(e));
    }
  }
  return (
    <main className="flex-1 space-y-6 p-5 sm:p-8">
      <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center">
        <div>
          <h1 className="font-serif text-2xl font-bold">Consultas</h1>
          <p className="text-sm text-muted-foreground">
            Agende e acompanhe os atendimentos.
          </p>
        </div>
        <Button onClick={nova} disabled={!pacientes.length}>
          <CalendarPlus /> Nova consulta</Button>
      </div>
      {erro && (
        <p
          role="alert"
          className="rounded-lg bg-destructive/10 p-3 text-sm text-destructive"
        >
          {erro}
        </p>
      )}
      {!pacientes.length && (
        <p className="rounded-lg border border-border bg-card p-3 text-sm text-muted-foreground">
          Cadastre um paciente ativo antes de agendar uma consulta.
        </p>
      )}
      {aberto && (
        <Card>
          <CardContent>
            <form onSubmit={salvar} className="grid gap-4 md:grid-cols-2">
              <div>
                <Label>Paciente</Label>
                <select
                  className={`${campo} mt-2`}
                  value={form.pacienteId}
                  onChange={(e) =>
                    setForm({ ...form, pacienteId: Number(e.target.value) })
                  }
                  required
                >
                  {pacientes.map((p) => (
                    <option key={p.id} value={p.id}>
                      {p.nome}
                    </option>
                  ))}
                </select>
              </div>
              <div>
                <Label>Data e hora</Label>
                <Input
                  className="mt-2 h-9"
                  type="datetime-local"
                  value={form.dataConsulta}
                  onChange={(e) =>
                    setForm({ ...form, dataConsulta: e.target.value })
                  }
                  required
                />
              </div>
              <div>
                <Label>Tipo</Label>
                <select
                  className={`${campo} mt-2`}
                  value={form.tipo}
                  onChange={(e) =>
                    setForm({ ...form, tipo: e.target.value as TipoConsulta })
                  }
                >
                  {Object.entries(tipos).map(([v, l]) => (
                    <option key={v} value={v}>
                      {l}
                    </option>
                  ))}
                </select>
              </div>
              <div className="md:col-span-2">
                <Label>Observações</Label>
                <textarea
                  className={`${campo} mt-2 min-h-20 py-2`}
                  value={form.observacoes}
                  onChange={(e) =>
                    setForm({ ...form, observacoes: e.target.value })
                  }
                />
              </div>
              <div className="flex gap-2 md:col-span-2">
                <Button type="submit">
                  {editando ? "Salvar alterações" : "Agendar"}
                </Button>
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setAberto(false)}
                >
                  Cancelar
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}
      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          className="h-9 pl-9"
          placeholder="Buscar paciente"
          value={busca}
          onChange={(e) => setBusca(e.target.value)}
        />
      </div>
      <Card>
        <CardContent className="overflow-x-auto px-0">
          <table className="w-full min-w-200 text-left text-sm">
            <thead className="border-b text-muted-foreground">
              <tr>
                <th className="p-4">Data</th>
                <th className="p-4">Paciente</th>
                <th className="p-4">Tipo</th>
                <th className="p-4">Status</th>
                <th className="p-4 text-right">Ações</th>
              </tr>
            </thead>
            <tbody>
              {filtradas.length === 0 ? (
                <tr>
                  <td
                    colSpan={5}
                    className="p-8 text-center text-muted-foreground"
                  >
                    Nenhuma consulta encontrada.
                  </td>
                </tr>
              ) : (
                filtradas.map((c) => (
                  <tr key={c.id} className="border-b last:border-0">
                    <td className="p-4 font-medium">
                      {formatar(c.dataConsulta)}
                    </td>
                    <td className="p-4">{c.pacienteNome}</td>
                    <td className="p-4">{tipos[c.tipo]}</td>
                    <td className="p-4">
                      <select
                        className="rounded-lg border border-input bg-background px-2 py-1 text-xs"
                        value={c.status}
                        onChange={(e) =>
                          void mudarStatus(
                            c.id,
                            e.target.value as StatusConsulta,
                          )
                        }
                      >
                        {Object.entries(status).map(([v, l]) => (
                          <option key={v} value={v}>
                            {l}
                          </option>
                        ))}
                      </select>
                    </td>
                    <td className="p-4">
                      <div className="flex justify-end gap-1">
                        <Button
                          size="icon-sm"
                          variant="ghost"
                          onClick={() => editar(c)}
                        >
                          <Pencil />
                        </Button>
                        <Button
                          size="icon-sm"
                          variant="ghost"
                          onClick={() => void excluir(c)}
                        >
                          <Trash2 className="text-destructive" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </CardContent>
      </Card>
    </main>
  );
}

import { useEffect, useMemo, useState, type FormEvent } from "react"
import { Pencil, Plus, Search, Trash2 } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { atualizarPaciente, atualizarStatusPaciente, criarPaciente, listarPacientes, mensagemDaApi, removerPaciente, type Paciente, type PacientePayload, type Sexo } from "@/lib/api"

const vazio: PacientePayload = { nome: "", dataNascimento: "", sexo: "FEMININO", email: "", telefone: "", observacoes: "" }
const campo = "min-h-9 w-full rounded-lg border border-input bg-transparent px-3 text-sm outline-none focus:border-ring focus:ring-3 focus:ring-ring/30"

function paraApi(data: string) { const [a, m, d] = data.split("-"); return `${d}/${m}/${a}` }
function paraInput(data: string) { const partes = data.split("/"); return partes.length === 3 ? `${partes[2]}-${partes[1]}-${partes[0]}` : data }

export default function Pacientes() {
  const [pacientes, setPacientes] = useState<Paciente[]>([])
  const [busca, setBusca] = useState("")
  const [form, setForm] = useState<PacientePayload>(vazio)
  const [editando, setEditando] = useState<number | null>(null)
  const [aberto, setAberto] = useState(false)
  const [erro, setErro] = useState<string | null>(null)
  const [carregando, setCarregando] = useState(true)

  async function carregar() { try { setPacientes(await listarPacientes()); setErro(null) } catch (e) { setErro(mensagemDaApi(e)) } finally { setCarregando(false) } }
  useEffect(() => { void carregar() }, [])

  const filtrados = useMemo(() => pacientes.filter((p) => `${p.nome} ${p.email}`.toLowerCase().includes(busca.toLowerCase())), [pacientes, busca])
  function abrirNovo() { setEditando(null); setForm(vazio); setAberto(true) }
  function abrirEdicao(p: Paciente) { setEditando(p.id); setForm({ nome: p.nome, dataNascimento: paraInput(p.dataNascimento), sexo: p.sexo, email: p.email, telefone: p.telefone ?? "", observacoes: p.observacoes ?? "" }); setAberto(true) }

  async function salvar(e: FormEvent) {
    e.preventDefault(); setErro(null)
    const payload = { ...form, dataNascimento: paraApi(form.dataNascimento) }
    try {
      if (editando) await atualizarPaciente(editando, payload)
      else await criarPaciente(payload)
      setAberto(false)
      await carregar()
    } catch (err) { setErro(mensagemDaApi(err)) }
  }

  async function alternar(p: Paciente) { try { await atualizarStatusPaciente(p.id, !p.ativo); await carregar() } catch (e) { setErro(mensagemDaApi(e)) } }
  async function excluir(p: Paciente) { if (!confirm(`Excluir ${p.nome}?`)) return; try { await removerPaciente(p.id); await carregar() } catch (e) { setErro(mensagemDaApi(e)) } }

  return <main className="flex-1 space-y-6 p-5 sm:p-8">
    <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center"><div><h1 className="font-serif text-2xl font-bold">Pacientes</h1><p className="text-sm text-muted-foreground">Gerencie os pacientes cadastrados.</p></div><Button onClick={abrirNovo}><Plus /> Novo paciente</Button></div>
    {erro && <p role="alert" className="rounded-lg bg-destructive/10 p-3 text-sm text-destructive">{erro}</p>}
    {aberto && <Card><CardContent><form onSubmit={salvar} className="grid gap-4 md:grid-cols-2">
      <div><Label>Nome</Label><Input className="mt-2 h-9" value={form.nome} onChange={(e) => setForm({...form,nome:e.target.value})} required /></div>
      <div><Label>E-mail</Label><Input className="mt-2 h-9" type="email" value={form.email} onChange={(e) => setForm({...form,email:e.target.value})} required /></div>
      <div><Label>Data de nascimento</Label><Input className="mt-2 h-9" type="date" value={form.dataNascimento} onChange={(e) => setForm({...form,dataNascimento:e.target.value})} required /></div>
      <div><Label>Sexo</Label><select className={`${campo} mt-2`} value={form.sexo} onChange={(e) => setForm({...form,sexo:e.target.value as Sexo})}><option value="FEMININO">Feminino</option><option value="MASCULINO">Masculino</option></select></div>
      <div><Label>Telefone</Label><Input className="mt-2 h-9" value={form.telefone} onChange={(e) => setForm({...form,telefone:e.target.value})} /></div>
      <div className="md:col-span-2"><Label>Observações</Label><textarea className={`${campo} mt-2 min-h-20 py-2`} value={form.observacoes} onChange={(e) => setForm({...form,observacoes:e.target.value})} /></div>
      <div className="flex gap-2 md:col-span-2"><Button type="submit">{editando ? "Salvar alterações" : "Cadastrar"}</Button><Button type="button" variant="outline" onClick={() => setAberto(false)}>Cancelar</Button></div>
    </form></CardContent></Card>}
    <div className="relative max-w-md"><Search className="absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted-foreground"/><Input className="h-9 pl-9" placeholder="Buscar por nome ou e-mail" value={busca} onChange={(e) => setBusca(e.target.value)}/></div>
    <Card><CardContent className="overflow-x-auto px-0"><table className="w-full min-w-175 text-left text-sm"><thead className="border-b text-muted-foreground"><tr><th className="p-4">Nome</th><th className="p-4">Contato</th><th className="p-4">Nascimento</th><th className="p-4">Status</th><th className="p-4 text-right">Ações</th></tr></thead><tbody>
      {carregando ? <tr><td colSpan={5} className="p-8 text-center text-muted-foreground">Carregando...</td></tr> : filtrados.length === 0 ? <tr><td colSpan={5} className="p-8 text-center text-muted-foreground">Nenhum paciente encontrado.</td></tr> : filtrados.map((p) => <tr key={p.id} className="border-b last:border-0"><td className="p-4 font-medium">{p.nome}</td><td className="p-4"><div>{p.email}</div><div className="text-xs text-muted-foreground">{p.telefone || "Sem telefone"}</div></td><td className="p-4">{p.dataNascimento}</td><td className="p-4"><button onClick={() => void alternar(p)} className={`rounded-full px-2.5 py-1 text-xs font-medium ${p.ativo ? "bg-primary/10 text-primary" : "bg-muted text-muted-foreground"}`}>{p.ativo ? "Ativo" : "Inativo"}</button></td><td className="p-4"><div className="flex justify-end gap-1"><Button size="icon-sm" variant="ghost" onClick={() => abrirEdicao(p)} aria-label="Editar"><Pencil/></Button><Button size="icon-sm" variant="ghost" onClick={() => void excluir(p)} aria-label="Excluir"><Trash2 className="text-destructive"/></Button></div></td></tr>)}
    </tbody></table></CardContent></Card>
  </main>
}

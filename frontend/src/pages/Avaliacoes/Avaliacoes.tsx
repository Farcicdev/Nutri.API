import { useEffect, useState, type FormEvent } from "react"
import { ClipboardPlus } from "lucide-react"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { criarAvaliacao, listarAvaliacoesPorConsulta, listarConsultas, mensagemDaApi, type Avaliacao, type AvaliacaoPayload, type Consulta, type ProtocoloDobras } from "@/lib/api"

const campo = "min-h-9 w-full rounded-lg border border-input bg-transparent px-3 text-sm outline-none focus:border-ring focus:ring-3 focus:ring-ring/30"
const medidas = [["cintura","Cintura (cm)"],["quadril","Quadril (cm)"],["peitoral","Peitoral (mm)"],["tricipital","Tricipital (mm)"],["subescapular","Subescapular (mm)"],["bicipital","Bicipital (mm)"],["suprailiaca","Supra-ilíaca (mm)"],["abdominal","Abdominal (mm)"],["coxa","Coxa (mm)"],["axilarMedia","Axilar média (mm)"]] as const
const protocolos:Record<ProtocoloDobras,string>={JACKSON_POLLACK_3_PONTOS:"Jackson & Pollock — 3 pontos",JACKSON_POLLACK_7_PONTOS:"Jackson & Pollock — 7 pontos",FAULKNER:"Faulkner"}
function paraApi(valor:string){const [data,hora]=valor.split("T");const[a,m,d]=data.split("-");return`${d}/${m}/${a} ${hora}`}
function formatar(valor:string){return new Intl.DateTimeFormat("pt-BR",{dateStyle:"short",timeStyle:"short"}).format(new Date(valor))}

export default function Avaliacoes(){
  const [consultas,setConsultas]=useState<Consulta[]>([]),[consultaId,setConsultaId]=useState(0),[avaliacoes,setAvaliacoes]=useState<Avaliacao[]>([])
  const [aberto,setAberto]=useState(false),[erro,setErro]=useState<string|null>(null)
  useEffect(()=>{listarConsultas().then(c=>{setConsultas(c);if(c[0])setConsultaId(c[0].id)}).catch(e=>setErro(mensagemDaApi(e)))},[])
  useEffect(()=>{if(!consultaId){setAvaliacoes([]);return} listarAvaliacoesPorConsulta(consultaId).then(setAvaliacoes).catch(e=>setErro(mensagemDaApi(e)))},[consultaId])
  async function salvar(event:FormEvent<HTMLFormElement>){event.preventDefault();const f=new FormData(event.currentTarget);const numero=(nome:string)=>{const v=f.get(nome)?.toString();return v?Number(v):undefined}
    const payload:AvaliacaoPayload={consultaId,peso:Number(f.get("peso")),altura:Number(f.get("altura")),dataAvaliacao:paraApi(String(f.get("dataAvaliacao"))),protocoloDobras:String(f.get("protocoloDobras")) as ProtocoloDobras,observacoes:String(f.get("observacoes")??"")}
    for(const [nome] of medidas){const valor=numero(nome);if(valor!==undefined)Object.assign(payload,{[nome]:valor})}
    try{await criarAvaliacao(payload);setAberto(false);setAvaliacoes(await listarAvaliacoesPorConsulta(consultaId));setErro(null)}catch(e){setErro(mensagemDaApi(e))}
  }
  return <main className="flex-1 space-y-6 p-5 sm:p-8">
    <div className="flex flex-col justify-between gap-4 sm:flex-row sm:items-center"><div><h1 className="font-serif text-2xl font-bold">Avaliações antropométricas</h1><p className="text-sm text-muted-foreground">Cadastre medidas e acompanhe os resultados calculados.</p></div><Button onClick={()=>setAberto(true)} disabled={!consultaId}><ClipboardPlus/>Nova avaliação</Button></div>
    {erro&&<p role="alert" className="rounded-lg bg-destructive/10 p-3 text-sm text-destructive">{erro}</p>}
    <div className="max-w-xl"><Label>Consulta</Label><select className={`${campo} mt-2`} value={consultaId} onChange={e=>setConsultaId(Number(e.target.value))}>{consultas.length===0&&<option value={0}>Nenhuma consulta disponível</option>}{consultas.map(c=><option key={c.id} value={c.id}>{c.pacienteNome} — {formatar(c.dataConsulta)}</option>)}</select></div>
    {aberto&&<Card><CardHeader><CardTitle>Nova avaliação</CardTitle></CardHeader><CardContent><form onSubmit={salvar} className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
      <div><Label>Peso (kg)</Label><Input name="peso" type="number" step="0.01" min="0.01" className="mt-2 h-9" required/></div><div><Label>Altura (m)</Label><Input name="altura" type="number" step="0.01" min="0.01" className="mt-2 h-9" required/></div><div><Label>Data</Label><Input name="dataAvaliacao" type="datetime-local" max={new Date().toISOString().slice(0,16)} className="mt-2 h-9" required/></div><div><Label>Protocolo</Label><select name="protocoloDobras" className={`${campo} mt-2`}>{Object.entries(protocolos).map(([v,l])=><option key={v} value={v}>{l}</option>)}</select></div>
      {medidas.map(([nome,label])=><div key={nome}><Label>{label}</Label><Input name={nome} type="number" step="0.01" min="0.01" className="mt-2 h-9"/></div>)}
      <div className="sm:col-span-2 lg:col-span-4"><Label>Observações</Label><textarea name="observacoes" className={`${campo} mt-2 min-h-20 py-2`}/></div>
      <p className="text-xs text-muted-foreground sm:col-span-2 lg:col-span-4">As dobras obrigatórias dependem do protocolo e do sexo do paciente. O backend informará quais estiverem faltando.</p>
      <div className="flex gap-2 sm:col-span-2 lg:col-span-4"><Button type="submit">Calcular e salvar</Button><Button type="button" variant="outline" onClick={()=>setAberto(false)}>Cancelar</Button></div>
    </form></CardContent></Card>}
    <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">{avaliacoes.length===0?<Card className="md:col-span-2 xl:col-span-3"><CardContent className="py-10 text-center text-muted-foreground">Nenhuma avaliação para esta consulta.</CardContent></Card>:avaliacoes.map(a=><Card key={a.id}><CardHeader><CardTitle className="flex justify-between"><span>{formatar(a.dataAvaliacao)}</span><span className="text-sm font-normal text-muted-foreground">{protocolos[a.protocoloDobras]}</span></CardTitle></CardHeader><CardContent className="grid grid-cols-2 gap-3"><Resultado label="IMC" valor={a.imc.toFixed(2)}/><Resultado label="Gordura" valor={`${a.percentualGordura.toFixed(2)}%`}/><Resultado label="Massa magra" valor={`${a.massaMagra.toFixed(2)} kg`}/><Resultado label="Massa gorda" valor={`${a.massaGorda.toFixed(2)} kg`}/><Resultado label="Peso" valor={`${a.peso.toFixed(2)} kg`}/><Resultado label="Altura" valor={`${a.altura.toFixed(2)} m`}/></CardContent></Card>)}</div>
  </main>
}
function Resultado({label,valor}:{label:string,valor:string}){return <div className="rounded-lg bg-muted p-3"><div className="text-xs text-muted-foreground">{label}</div><div className="mt-1 font-semibold">{valor}</div></div>}
